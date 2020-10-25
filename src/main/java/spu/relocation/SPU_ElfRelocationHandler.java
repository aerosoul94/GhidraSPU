package spu.relocation;

import ghidra.app.util.bin.format.elf.*;
import ghidra.app.util.bin.format.elf.relocation.ElfRelocationContext;
import ghidra.app.util.bin.format.elf.relocation.ElfRelocationHandler;
import ghidra.program.model.address.Address;
import ghidra.program.model.listing.Program;
import ghidra.program.model.mem.Memory;
import ghidra.program.model.mem.MemoryAccessException;
import ghidra.util.exception.NotFoundException;

public class SPU_ElfRelocationHandler extends ElfRelocationHandler {
    @Override
    public boolean canRelocate(ElfHeader elf) {
        return elf.e_machine() == SPU_ElfConstants.EM_SPU;
    }

    @Override
    public void relocate(ElfRelocationContext elfRelocationContext, ElfRelocation relocation,
                    Address relocationAddress) throws MemoryAccessException, NotFoundException {
        Program program = elfRelocationContext.getProgram();
        Memory memory = program.getMemory();

        int offset = (int)relocationAddress.getOffset();
        int symbolIndex = relocation.getSymbolIndex();
        int type = relocation.getType();
        int addend = (int)relocation.getAddend();

        ElfSymbol sym = elfRelocationContext.getSymbol(symbolIndex);
        int symbolValue = (int)elfRelocationContext.getSymbolValue(sym);
        
        int oldValue = memory.getInt(relocationAddress);
        int newValue = 0;

        switch (type) {
            case SPU_ElfRelocationConstants.R_SPU_ADDR10:
                newValue = (symbolValue + addend) >> 4;
                newValue = (oldValue & ~SPU_ElfRelocationConstants.SPU_I10) | (newValue << 14);
                memory.setInt(relocationAddress, newValue);
                break;
            case SPU_ElfRelocationConstants.R_SPU_ADDR32:
                newValue = (symbolValue + addend);
                memory.setInt(relocationAddress, newValue);
                break;
            case SPU_ElfRelocationConstants.R_SPU_ADDR16:
                newValue = (symbolValue + addend) >> 2;
                newValue = (oldValue & ~SPU_ElfRelocationConstants.SPU_I16) | (newValue << 7);
                memory.setInt(relocationAddress, newValue);
                break;
            case SPU_ElfRelocationConstants.R_SPU_REL16:
                newValue = (symbolValue + addend - offset) >> 2;
                newValue = (oldValue & ~SPU_ElfRelocationConstants.SPU_I16) | (newValue << 7);
                memory.setInt(relocationAddress, newValue);
                break;
            case SPU_ElfRelocationConstants.R_SPU_ADDR16_HI:
                newValue = ((symbolValue + addend) >> 16) & 0xffff;
                newValue = (oldValue & ~SPU_ElfRelocationConstants.SPU_I16) | (newValue << 7);
                memory.setInt(relocationAddress, newValue);
                break;
            case SPU_ElfRelocationConstants.R_SPU_ADDR16_LO:
                newValue = (symbolValue + addend) & 0xffff;
                newValue = (oldValue & ~SPU_ElfRelocationConstants.SPU_I16) | (newValue << 7);
                memory.setInt(relocationAddress, newValue);
                break;
            case SPU_ElfRelocationConstants.R_SPU_ADDR18:
                newValue = (symbolValue + addend);
                newValue = (oldValue & ~SPU_ElfRelocationConstants.SPU_I18) | (newValue << 7);
                memory.setInt(relocationAddress, newValue);
                break;
            case SPU_ElfRelocationConstants.R_SPU_ADDR7:
                newValue = (symbolValue + addend);
                newValue = (oldValue & ~SPU_ElfRelocationConstants.SPU_I7) | (newValue << 14);
                memory.setInt(relocationAddress, newValue);
                break;
            case SPU_ElfRelocationConstants.R_SPU_ADDR10I:
                newValue = (symbolValue + addend);
                newValue = (oldValue & ~SPU_ElfRelocationConstants.SPU_I10) | (newValue << 14);
                memory.setInt(relocationAddress, newValue);
                break;
            case SPU_ElfRelocationConstants.R_SPU_ADDR16I:
                newValue = (symbolValue + addend);
                newValue = (oldValue & ~SPU_ElfRelocationConstants.SPU_I16) | (newValue << 7);
                memory.setInt(relocationAddress, newValue);
                break;
            case SPU_ElfRelocationConstants.R_SPU_REL32:
                newValue = (symbolValue + addend - offset);
                memory.setInt(relocationAddress, newValue);
                break;
            case SPU_ElfRelocationConstants.R_SPU_REL9:
                newValue = (symbolValue + addend - offset) >> 2;
                newValue = (oldValue & ~SPU_ElfRelocationConstants.SPU_I9) | 
                    ((newValue & 0x7f) | ((newValue & 0x180) << 23));
                memory.setInt(relocationAddress, newValue);
                break;
            case SPU_ElfRelocationConstants.R_SPU_REL9I:
                newValue = (symbolValue + addend - offset) >> 2;
                newValue = (oldValue & ~SPU_ElfRelocationConstants.SPU_I9I) | 
                    ((newValue & 0x7f) | ((newValue & 0x180) << 14));
                memory.setInt(relocationAddress, newValue);
                break;
            case SPU_ElfRelocationConstants.R_SPU_ADDR16X:
                // Same as ADDR16 just without the right shift
                newValue = (symbolValue + addend);
                newValue = (oldValue & ~SPU_ElfRelocationConstants.SPU_I16) | (newValue << 7);
                memory.setInt(relocationAddress, newValue);
                break;
            case SPU_ElfRelocationConstants.R_SPU_ADD_PIC:
                // ??? change a rt,ra,rb to ai rt,ra,0
                // op[0] = 0x1c, op[1] = 0x00, op[2] &= 0x3f
                newValue = (oldValue & ~0xff000000) | (0x1c << 24);
                newValue = (newValue & ~0x00ff0000);
                newValue = (newValue & ~0x0000ff00) | ((newValue & 0x0000ff00) & (0x3f << 8));
                memory.setInt(relocationAddress, newValue);
                break;
            /* Currently unhandled relocations */
            case SPU_ElfRelocationConstants.R_SPU_PPU32:
            case SPU_ElfRelocationConstants.R_SPU_PPU64:
                /* These should be setting relocations in PPU memory */
            default:
                String symbolName = sym.getNameAsString();
                markAsUnhandled(program, relocationAddress, type, symbolIndex, symbolName,
                        elfRelocationContext.getLog());
                break;
        }
    }
}