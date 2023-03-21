package de.neariyeveryone.utilities;

import net.minecraftforge.energy.EnergyStorage;

public abstract class NEnergyStorage extends EnergyStorage {

    public NEnergyStorage(int capacity) {
        super(capacity);
    }

    public NEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public NEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public NEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int extractedEnergy = super.extractEnergy(maxExtract, simulate);
        if (extractedEnergy != 0)
            onEnergyChange();
        return extractedEnergy;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int receivedEnergy = super.receiveEnergy(maxReceive, simulate);
        if (receivedEnergy != 0)
            onEnergyChange();
        return receivedEnergy;
    }

    public int setEnergy(int energy) {
        this.energy = energy;
        return energy;
    }

    public abstract void onEnergyChange();
}
