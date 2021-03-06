/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.api.part;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.vec.Vector3;

public class RedstoneConnection {

    private BPPartFace part;

    private boolean isInput = true;

    private int power = 0;

    private boolean isEnabled = false;

    private String id = "";

    public RedstoneConnection(BPPartFace part, String id) {

        this.part = part;
        this.id = id;
    }

    public RedstoneConnection(BPPartFace part, String id, boolean isInput) {

        this(part, id);
        this.isInput = isInput;
    }

    public RedstoneConnection(BPPartFace part, String id, boolean isInput, boolean isEnabled) {

        this(part, id, isInput);
        this.isEnabled = isEnabled;
    }

    public void setPart(BPPartFace part) {

        this.part = part;
    }

    public void setInput() {

        if (!isInput) {
            isInput = true;
            if (part != null)
                part.notifyUpdate();
        }
    }

    public void setOutput() {

        if (isInput) {
            isInput = false;
            if (part != null)
                part.notifyUpdate();
        }
    }

    public boolean isInput() {

        return isInput;
    }

    public boolean isOutput() {

        return !isInput;
    }

    public void enable() {

        boolean was = isEnabled;

        isEnabled = true;
        if (part != null) {
            part.notifyUpdate();
            if (!was)
                part.notifyUpdate();
        }
    }

    public void disable() {

        boolean was = isEnabled;

        isEnabled = false;
        if (was && part != null)
            part.notifyUpdate();
    }

    public boolean isEnabled() {

        return isEnabled;
    }

    public void setPower(int power) {

        setPower(power, true);
    }

    public void setPower(int power, boolean notifyUpdate) {

        int last = this.power;

        this.power = power;

        if (last != power && part != null && notifyUpdate) {
            part.notifyUpdate();
            if (part.getWorld() != null) {
                Vector3 loc = new Vector3(part.getX(), part.getY(), part.getZ(), part.getWorld());
                for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                    Vector3 v = loc.getRelative(d);
                    part.getWorld().notifyBlockChange(v.getBlockX(), v.getBlockY(), v.getBlockZ(), loc.getBlock());
                    part.getWorld().markBlockForUpdate(v.getBlockX(), v.getBlockY(), v.getBlockZ());
                }
            }
        }
    }

    public int getPower() {

        return power;
    }

    public void setID(String id) {

        this.id = id;
    }

    public String getID() {

        return id;
    }

    public NBTTagCompound getNBTTag() {

        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("isInput", isInput);
        tag.setBoolean("isEnabled", isEnabled);
        tag.setInteger("power", power);
        tag.setString("id", id);
        return tag;
    }

    public void load(NBTTagCompound tag) {

        isInput = tag.getBoolean("isInput");
        isEnabled = tag.getBoolean("isEnabled");
        power = tag.getInteger("power");
        id = tag.getString("id");
    }

}
