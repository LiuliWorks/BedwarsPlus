package cn.ricoco.funframework.map;

import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.entity.EntityExplodeEvent;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.ricoco.bedwarsplus.Main;
import cn.ricoco.funframework.game.Room;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapProtect implements Listener {
    public static ArrayList<String> protectedMaps=new ArrayList<>();
    public static Map<String,ArrayList<Position>> blockList=new HashMap<>();
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event){
        String levelName=event.getBlock().level.getName();
        if(protectedMaps.contains(levelName)){
            ArrayList<Position> bList=blockList.get(levelName);
            Block thisBlock=event.getBlock();
            bList.add(Position.fromObject(new Vector3(thisBlock.x,thisBlock.y, thisBlock.z), thisBlock.level));
            blockList.put(levelName,bList);
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event){
        if(!checkBlockCanBreak(event.getBlock())){
            event.setCancelled();
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityExplode(EntityExplodeEvent event){
        List<Block> blockList=event.getBlockList();
        blockList.removeIf(block -> !checkBlockCanBreak(block));
        event.setBlockList(blockList);
    }
    public static void addProtectMap(String levelName){
        protectedMaps.add(levelName);
        blockList.put(levelName,new ArrayList<>());
    }
    public static void removeProtectMap(String levelName){
        protectedMaps.remove(levelName);
        blockList.remove(levelName);
    }
    public static Boolean checkBlockCanBreak(Block block){
        String levelName=block.level.getName();
        if(protectedMaps.contains(levelName)) {
            ArrayList<Position> bList = blockList.get(levelName);
            return bList.contains(Position.fromObject(new Vector3(block.x, block.y, block.z), block.level));
        }
        return true;
    }
}
