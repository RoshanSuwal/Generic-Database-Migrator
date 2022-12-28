package org.ekbana.cassandraMigrator.proximity;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GeoUtils {

    public static final double EARTH_RADIUS = 6371000; // int meter

    public static class LatLng {
        private double lat;
        private double lng;

        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }

        public double getLatInRad() {
            return Math.toRadians(lat);
        }

        public double getLngInRadian() {
            return Math.toRadians(lng);
        }

        public LatLng(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }
    }

    public static double geoDistance(LatLng source, LatLng destination) {
        double deltaLat = destination.getLatInRad() - source.getLatInRad();
        double deltaLng = destination.getLngInRadian() - source.getLngInRadian();

        double a = Math.pow(Math.sin(deltaLat / 2.0), 2)
                + Math.cos(source.getLatInRad()) * Math.cos(destination.getLatInRad()) * Math.pow(Math.sin(deltaLng / 2), 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    public static String encodeGeoHash(LatLng latLng, int level) {
        return Long.toHexString(encodeGeoHash(latLng.getLat(), latLng.getLng(), level));
    }

    public static long encodeGeoHash(double lat, double lng, int level) {
        double minLat = -90, maxLat = 90;
        double minLng = -180, maxLng = 180;

        long result = 0;

        for (int i = 0; i < level*2; i++) {
            if (i % 2 == 0) {
                double midPoint = (minLng + maxLng) / 2;
                if (lng < midPoint) {
                    result <<= 1;
                    maxLng = midPoint;
                } else {
                    result = result << 1 | 1;
                    minLng = midPoint;
                }
            } else {
                double midPoint = (minLat + maxLat) / 2;
                if (lat < midPoint) {
                    result <<= 1;
                    maxLat = midPoint;
                } else {
                    result = result << 1 | 1;
                    minLat = midPoint;
                }
            }
        }
        return result;
    }

    public static LatLng decodeGeoHash(String geoHash, int level) {
        return decodeGeoHash(Long.parseLong(geoHash, 16), level);
    }

    public static LatLng decodeGeoHash(long geoHash, int level) {
        double minLat = -90, maxLat = 90;
        double minLng = -180, maxLng = 180;

        for (int i = level*2 - 1; i >= 0; i--) {
            if ((1 & (geoHash >> i)) > 0) {
                if (i % 2 != 0) {
                    minLng = (minLng + maxLng) / 2;
                } else {
                    minLat = (minLat + maxLat) / 2;
                }
            } else {
                if (i % 2 != 0) {
                    maxLng = (minLng + maxLng) / 2;
                } else {
                    maxLat = (minLat + maxLat) / 2;
                }
            }
        }


        return new LatLng((minLat + maxLat) / 2, (minLng + maxLng) / 2);
    }


    public static long modifyBit(long n, int p, int b) {
        return (n & ~(1 << p)) | ((b << p) & (1 << p));
    }

    public static long calculate(long currentHash, int pos, int gt0) {
        return (currentHash & (1 << pos)) > 0
                ? modifyBit(currentHash, 1, gt0)
                : modifyBit(currentHash, 1, ~gt0);
    }

    public static boolean isSetBit(long currentHash, int pos) {
        return (currentHash & (1 << pos)) > 0;
    }

    public static long north(long currentHash, int offset) {

        if (isSetBit(currentHash, offset + 1)) {
            final long bit1 = modifyBit(currentHash, offset + 1, 0);
            return isSetBit(currentHash, offset + 3)
                    ? modifyBit(bit1, offset + 3, 0)
                    : modifyBit(bit1, offset + 3, 1);
        } else
            return modifyBit(currentHash, offset + 1, 1);
    }

    public static long south(long currentHash, int offset) {

        final long north = north(currentHash, offset);

        return isSetBit(north, offset + 3)
                ? modifyBit(north, offset + 3, 0)
                : modifyBit(north, offset + 3, 1);

    }

    public static long east(long currentHash, int offset) {
        if (isSetBit(currentHash, offset + 0))
            return (isSetBit(currentHash, offset + 2))
                    ? modifyBit(modifyBit(currentHash, offset + 0, 0), offset + 2, 0)
                    : modifyBit(modifyBit(currentHash, offset + 0, 0), offset + 2, 1);
        else
            return modifyBit(currentHash, offset + 0, 1);
    }

    public static long west(long currentHash, int offset) {
        final long east = east(currentHash, offset);

        return isSetBit(east, offset + 2)
                ? modifyBit(east, offset + 2, 0)
                : modifyBit(east, offset + 2, 1);
    }

    public static List<String> neighbours(String geoHash, int uplevel){
        return Arrays.stream(neighbours(Long.parseLong(geoHash,16),uplevel)).mapToObj(n->Long.toHexString(n)).collect(Collectors.toList());
    }
    public static long[] neighbours(long geoHash,int uplevel) {

        long currentHash=geoHash >> (uplevel*2);
        long[] neighbours = new long[9];

        neighbours[1] = north(currentHash, 0);
        neighbours[2] = east(neighbours[1], 0);
        neighbours[0] = west(neighbours[1], 0);

        neighbours[3] = west(currentHash, 0);
        neighbours[4] = currentHash;
        neighbours[5] = east(currentHash, 0);
        neighbours[7] = south(currentHash, 0);
        neighbours[6] = west(neighbours[7], 0);
        neighbours[8] = east(neighbours[7], 0);

        return neighbours;
    }
}
