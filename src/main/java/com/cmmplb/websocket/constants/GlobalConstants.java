package com.cmmplb.websocket.constants;

public interface GlobalConstants {
    char LEFT = '{';
    char RIGHT = '}';
    byte YES = 1;
    byte NO = 0;
    boolean Y = true;
    boolean N = false;
    int NUM_Z = 0;
    int NUM_O = 1;
    int NUM_T = 2;
    int NUM_TH = 3;
    int NUM_F = 4;
    int NUM_FI = 5;
    int NUM_SI = 6;
    int NUM_SE = 7;
    int NUM_EI = 8;
    byte NUM_ZERO = 0;
    byte NUM_ONE = 1;
    byte NUM_TWO = 2;
    byte NUM_THREE = 3;
    byte NUM_FOUR = 4;
    byte NUM_FIVE = 5;
    byte NUM_SIX = 6;
    byte NUM_SEVEN = 7;
    byte NUM_EIGHT = 8;
    byte NUM_NINE = 9;
    byte NUM_TEN = 10;
    byte NUM_ELEVEN = 11;
    byte NUM_TWELVE = 12;
    byte NUM_THIRTEEN = 13;
    byte NUM_FOURTEEN = 14;
    byte NUM_FIFTEEN = 15;
    byte NUM_SIXTEEN = 16;
    byte NUM_SEVENTEEN = 17;
    byte NUM_EIGHTEEN = 18;
    byte NUM_NINETEEN = 19;
    byte NUM_TWENTY = 20;

    public static enum EnableEnum {
        ENABLE((byte)0, true),
        DISABLE((byte)1, false);

        private final Byte value;
        private final Boolean enable;

        public static Boolean getEnable(Byte value) {
            EnableEnum[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                EnableEnum enableEnum = var1[var3];
                if (enableEnum.getValue().equals(value)) {
                    return enableEnum.enable;
                }
            }

            return DISABLE.enable;
        }

        public Byte getValue() {
            return this.value;
        }

        public Boolean getEnable() {
            return this.enable;
        }

        private EnableEnum(Byte value, Boolean enable) {
            this.value = value;
            this.enable = enable;
        }
    }

    public static enum DeletedEnum {
        N((byte)0, false),
        Y((byte)1, true);

        private final Byte value;
        private final Boolean deleted;

        public Byte getValue() {
            return this.value;
        }

        public Boolean getDeleted() {
            return this.deleted;
        }

        private DeletedEnum(Byte value, Boolean deleted) {
            this.value = value;
            this.deleted = deleted;
        }
    }

    public static enum StatusEnum {
        ENABLE((byte)0, true),
        DISABLE((byte)1, false);

        private final Byte value;
        private final Boolean status;

        public static Boolean getStatus(Byte value) {
            StatusEnum[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                StatusEnum statusEnum = var1[var3];
                if (statusEnum.getValue().equals(value)) {
                    return statusEnum.status;
                }
            }

            return DISABLE.status;
        }

        public Byte getValue() {
            return this.value;
        }

        public Boolean getStatus() {
            return this.status;
        }

        private StatusEnum(Byte value, Boolean status) {
            this.value = value;
            this.status = status;
        }
    }
}