package net.minecraft.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Panel;
import java.awt.TextArea;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.minecraft.client.CanvasCrashReport;
import net.minecraft.client.CanvasMojangLogo;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class PanelCrashReport extends Panel {

   public PanelCrashReport(CrashReport p_i3010_1_) {
      this.setBackground(new Color(3028036));
      this.setLayout(new BorderLayout());
      StringWriter var2 = new StringWriter();
      p_i3010_1_.func_71505_b().printStackTrace(new PrintWriter(var2));
      String var3 = var2.toString();
      String var4 = "";
      String var5 = "";

      try {
         var5 = var5 + "Generated " + (new SimpleDateFormat()).format(new Date()) + "\n";
         var5 = var5 + "\n";
         var5 = var5 + p_i3010_1_.func_90021_c();
         var4 = GL11.glGetString(7936);
      } catch (Throwable var9) {
         var5 = var5 + "[failed to get system properties (" + var9 + ")]\n";
      }

      var5 = var5 + "\n\n";
      var5 = var5 + var3;
      String var6 = "";
      var6 = var6 + "\n";
      var6 = var6 + "\n";
      if(var3.contains("Pixel format not accelerated")) {
         var6 = var6 + "      Bad video card drivers!      \n";
         var6 = var6 + "      -----------------------      \n";
         var6 = var6 + "\n";
         var6 = var6 + "Minecraft was unable to start because it failed to find an accelerated OpenGL mode.\n";
         var6 = var6 + "This can usually be fixed by updating the video card drivers.\n";
         if(var4.toLowerCase().contains("nvidia")) {
            var6 = var6 + "\n";
            var6 = var6 + "You might be able to find drivers for your video card here:\n";
            var6 = var6 + "  http://www.nvidia.com/\n";
         } else if(var4.toLowerCase().contains("ati")) {
            var6 = var6 + "\n";
            var6 = var6 + "You might be able to find drivers for your video card here:\n";
            var6 = var6 + "  http://www.amd.com/\n";
         }
      } else {
         var6 = var6 + "      Minecraft has crashed!      \n";
         var6 = var6 + "      ----------------------      \n";
         var6 = var6 + "\n";
         var6 = var6 + "Minecraft has stopped running because it encountered a problem; " + p_i3010_1_.func_71501_a() + "\n\n";
         File var7 = p_i3010_1_.func_71497_f();
         if(var7 == null) {
            p_i3010_1_.func_71508_a(new File(new File(Minecraft.func_71380_b(), "crash-reports"), "crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-client.txt"), Minecraft.func_71410_x().func_98033_al());
            var7 = p_i3010_1_.func_71497_f();
         }

         if(var7 != null) {
            String var8 = var7.getAbsolutePath();
            var6 = var6 + "A full error report has been saved to " + var8 + " - Please include a copy of that file (Not this screen!) if you report this crash to anyone; without it, they will not be able to help fix the crash :(";
            var5 = "Full report at:\n" + var8 + "\nPlease show that file to Mojang, NOT just this screen!\n\n" + var5;
         } else {
            var6 = var6 + "We were unable to save this report to a file.";
         }

         var6 = var6 + "\n";
      }

      var6 = var6 + "\n";
      var6 = var6 + "\n";
      var6 = var6 + "\n";
      var6 = var6 + "--- BEGIN ERROR REPORT " + Integer.toHexString(var6.hashCode()) + " --------\n";
      var6 = var6 + var5;
      var6 = var6 + "--- END ERROR REPORT " + Integer.toHexString(var6.hashCode()) + " ----------\n";
      var6 = var6 + "\n";
      var6 = var6 + "\n";
      TextArea var10 = new TextArea(var6, 0, 0, 1);
      var10.setFont(new Font("Monospaced", 0, 12));
      this.add(new CanvasMojangLogo(), "North");
      this.add(new CanvasCrashReport(80), "East");
      this.add(new CanvasCrashReport(80), "West");
      this.add(new CanvasCrashReport(100), "South");
      this.add(var10, "Center");
   }
}
