/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.part.gate;

import com.bluepowermod.api.part.FaceDirection;
import com.bluepowermod.api.part.RedstoneConnection;
import com.bluepowermod.client.renderers.RenderHelper;
import com.bluepowermod.util.Refs;

import net.minecraft.util.AxisAlignedBB;

import java.util.List;

public class GatePulseFormer extends GateBase {
    
    private boolean power[] = new boolean[4];
    
    @Override
    public void initializeConnections(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        // Init front
        front.enable();
        front.setOutput();
        
        // Init left
        left.disable();
        
        // Init back
        back.enable();
        back.setInput();
        
        // Init right
        right.disable();
    }
    
    @Override
    public String getGateID() {
    
        return "pulseformer";
    }
    
    @Override
    public void renderTop(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right, float frame) {
    
        RenderHelper.renderRedstoneTorch(-3 / 16D, 1D / 8D, 1 / 16D, 9D / 16D, !power[0]);
        RenderHelper.renderRedstoneTorch(3 / 16D, 1D / 8D, 1 / 16D, 9D / 16D, power[2]);
        RenderHelper.renderRedstoneTorch(0, 1D / 8D, -5 / 16D, 9D / 16D, !power[2] && power[1]);

        if(!power[1]){
            renderTopTexture(Refs.MODID + ":textures/blocks/gates/" + getType() + "/center_on.png");
        }else{
            renderTopTexture(Refs.MODID + ":textures/blocks/gates/" + getType() + "/center_off.png");
        }
        renderTopTexture(FaceDirection.BACK, power[0]);
        renderTopTexture(FaceDirection.LEFT, !power[1]);
        renderTopTexture(FaceDirection.RIGHT, power[2]);
    }
    
    @Override
    public void addOcclusionBoxes(List<AxisAlignedBB> boxes) {
    
        super.addOcclusionBoxes(boxes);
    }
    
    @Override
    public void doLogic(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        power[3] = power[2];
        power[2] = power[1];
        power[1] = power[0];
        power[0] = back.getPower() > 0;
        
        if (!power[2] && power[1]) {
            front.setPower(15);
        } else {
            front.setPower(0);
        }
        
    }
    
    @Override
    public void addWailaInfo(List<String> info) {
    
    }
    
}
