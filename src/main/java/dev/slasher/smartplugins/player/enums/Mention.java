package dev.slasher.smartplugins.player.enums;

    public enum Mention {
        ATIVADO,
        DESATIVADO;

        public String getInkSack() {
            if (this == ATIVADO) {
                return "10";
            }

            return "8";
        }

        public String getName() {
            if (this == ATIVADO) {
                return "§aAtivado";
            }

            return "§cDesativado";
        }

        public Mention next() {
            if (this == DESATIVADO) {
                return ATIVADO;
            }

            return DESATIVADO;
        }

        private static final Mention[] VALUES = values();

        public static Mention getByOrdinal(long ordinal) {
            if (ordinal < 2 && ordinal > -1) {
                return VALUES[(int) ordinal];
            }

            return null;
        }
    }


