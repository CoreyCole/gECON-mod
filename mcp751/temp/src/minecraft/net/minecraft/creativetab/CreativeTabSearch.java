package net.minecraft.creativetab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

final class CreativeTabSearch extends CreativeTabs {

   CreativeTabSearch(int p_i3638_1_, String p_i3638_2_) {
      super(p_i3638_1_, p_i3638_2_);
   }

   @SideOnly(Side.CLIENT)
   public int func_78012_e() {
      return Item.field_77750_aQ.field_77779_bT;
   }
}
