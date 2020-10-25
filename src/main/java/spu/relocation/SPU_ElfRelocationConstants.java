package spu.relocation;

public class SPU_ElfRelocationConstants {
    public static final int R_SPU_NONE = 0;
    public static final int R_SPU_ADDR10 = 1;   // I10* (S + A) >> 4
    public static final int R_SPU_ADDR16 = 2;   // I16* (S + A) >> 2
    public static final int R_SPU_ADDR16_HI = 3;    // I16 #hi(S + A)
    public static final int R_SPU_ADDR16_LO = 4;    // I16 #lo(S + A)
    public static final int R_SPU_ADDR18 = 5;   // I18* S + A
    public static final int R_SPU_ADDR32 = 6;   // word32 S + A
    public static final int R_SPU_GLOB_DAT = 6;
    public static final int R_SPU_REL16 = 7;    // I16* (S + A - P) >> 2
    public static final int R_SPU_ADDR7 = 8;    // I7 (S + A)
    public static final int R_SPU_REL9 = 9;     // I9* (S + A - P) >> 2
    public static final int R_SPU_REL9I = 10;   // I9I* (S + A - P) >> 2
    public static final int R_SPU_ADDR10I = 11; // I10* S + A
    public static final int R_SPU_ADDR16I = 12; // I16* S + A
    public static final int R_SPU_REL32 = 13;   // word32 S + A - P
    public static final int R_SPU_ADDR16X = 14; // I16* S + A
    public static final int R_SPU_PPU32 = 15;   // word32 S + A
    public static final int R_SPU_PPU64 = 16;   // word64 S + A
    public static final int R_SPU_ADD_PIC = 17; // ??? change a rt,ra,rb to ai rt,ra,0

    public static final int SPU_I7 = 0x001fc000;
    public static final int SPU_I9 = 0x0180007f;
    public static final int SPU_I9I = 0x0000c07f;
    public static final int SPU_I10 = 0x00ffc000;
    public static final int SPU_I16 = 0x007fff80;
    public static final int SPU_I18 = 0x01ffff80;

    private SPU_ElfRelocationConstants() {
        // no construct
    }
}