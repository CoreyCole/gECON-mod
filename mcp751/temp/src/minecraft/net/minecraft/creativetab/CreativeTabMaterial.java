package net.minecraft.creativetab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

final class CreativeTabMaterial extends CreativeTabs {

   CreativeTabMaterial(int p_i3632_1_, String p_i3632_2_) {
      super(p_i3632_1_, p_i3632_2_);
   }

   @SideOnly(Side.CLIENT)
   public int func_78012_e() {
      return Item.field_77669_D.field_77779_bT;
   }
}
