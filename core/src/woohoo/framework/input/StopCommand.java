/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package woohoo.framework.input;

import com.badlogic.ashley.core.Entity;
import woohoo.gameobjects.components.MovementComponent;
import woohoo.gameworld.Mappers;

/**
 *
 * @author 17jwoo
 */
public class StopCommand implements InputCommand
{
    @Override
    public void execute(Entity entity) 
    {
		if (!Mappers.movements.has(entity) || !Mappers.inputs.has(entity)) return;

		Mappers.movements.get(entity).direction = MovementComponent.Direction.None;
        Mappers.inputs.get(entity).states.clear();
    }   
}
