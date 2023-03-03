package net.polar.utils;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public final class IDConstants {

    public static final long LOGGER_ID = 1076957692419113014L;
    public static final long GUILD_ID = 1076957691068555274L;
    public static final long VERIFICATION_ID = 1076967923421282414L;
    public static final long WELCOME_ID = 1076957691869667445L;
    public static final long OWNER_ROLE = 1076957691131461645L;
    public static final long RULES_ID = 1076957691869667446L;
    public static final long GENERAL_ID = 1076957692154876024L;
    public static final long REACTION_ROLES_ID = 1076957691869667448L;
    public static final long ADMIN_ROLE = 1076957691131461644L;
    public static final long STAFF_ROLE = 1076964501087322123L;
    public static final long ANNOUNCEMENTS_CHANNEL = 1076957692419113016L;
    public static final long RULES_CHANNEL = 1076957691869667446L;
    public static final long MEMBER_ROLE = 1076957691106299956L;
    public static final long INFO_CHANNEL = 1076957691869667447L;
    public static final long LEVEL_UP_CHANNEL = 1076957692154876028L;

    public enum LevelRoles {

        LEVEL_10(1076957691106299960L),
        LEVEL_20(1076957691106299961L),
        LEVEL_30(1076957691106299962L),
        LEVEL_40(1076957691106299963L)
        ;

        private static final LevelRoles[] VALUES = values();
        private final long id;

        LevelRoles(long id) {
            this.id = id;
        }

        public long getId() {
            return id;
        }

        public static @Nullable LevelRoles getByLevel(int level) {
            return Arrays.stream(VALUES).filter(levelRole -> levelRole.name().equals("LEVEL_" + level)).findFirst().orElse(null);
        }

    }

    public enum ReactionRoles {
        MALE(1080810169560223784L),
        FEMALE(1080810169560223784L),
        OTHER(1080810169560223784L)
        ;

        public static final ReactionRoles[] VALUES = values();

        private final long id;

        ReactionRoles(long id) {
            this.id = id;
        }

        public long getId() {
            return id;
        }

        public static @Nullable ReactionRoles getByButton(String buttonId) {
            return Arrays.stream(VALUES).filter(reactionRole -> reactionRole.name().equalsIgnoreCase(buttonId)).findFirst().orElse(null);
        }
    }

    public enum ColorRoles {

        BLUE(1076957691106299955L),
        RED(1076957691068555283L),
        PURPLE(1076957691068555282L),
        GREEN(1076957691068555281L),
        YELLOW(1076957691068555280L),
        ORANGE(1076957691068555279L),
        GRAY(1076957691068555278L)
        ;
        public static final ColorRoles[] VALUES = values();

        private final long id;
        ColorRoles(long id) {
            this.id = id;
        }

        public long getId() {
            return id;
        }

        public static @Nullable ColorRoles getByButton(String buttonId) {
            return Arrays.stream(VALUES).filter(colorRole -> colorRole.name().equalsIgnoreCase(buttonId)).findFirst().orElse(null);
        }
    }

}
