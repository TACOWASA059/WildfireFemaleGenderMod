/*
    Wildfire's Female Gender Mod is a female gender mod created for Minecraft.
    Copyright (C) 2023 WildfireRomeo

    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
/*
    Modifications:
    - 2025-03-03: tacowasa059 - Added breast width and height settings
    - 2025-03-04: tacowasa059 - Added Hip Renderer
*/
package com.wildfire.render;

import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;

public class WildfireModelRenderer {

	public static class ModelBox {
		public WildfireModelRenderer.TexturedQuad[] quads;
		public float posX1;
		public float posY1;
		public float posZ1;
		public float posX2;
		public float posY2;
		public float posZ2;

		public ModelBox(float tW, float tH, float texU, float texV, float x, float y, float z, float dx, float dy, float dz, float delta, boolean mirror) {
			this(tW, tH, texU, texV, x, y, z, dx, dy, dz, delta, mirror, 5);
		}

		protected ModelBox(float tW, float tH, float texU, float texV, float x, float y, float z, float dx, float dy, float dz, float delta, boolean mirror, int quads) {
			this(tW, tH, texU, texV, x, y, z, dx, dy, dz, delta, mirror, quads, false);
		}

		protected ModelBox(float tW, float tH, float texU, float texV, float x, float y, float z, float dx, float dy, float dz, float delta, boolean mirror, int quads, boolean extra) {
			this.posX1 = x;
			this.posY1 = y;
			this.posZ1 = z;
			this.posX2 = x + dx;
			this.posY2 = y + dy;
			this.posZ2 = z + dz;
			this.quads = new TexturedQuad[quads];
			float f = x + dx;
			float f1 = y + dy;
			float f2 = z + dz;
			x = x - delta;
			y = y - delta;
			z = z - delta;
			f = f + delta;
			f1 = f1 + delta;
			f2 = f2 + delta;
			if (mirror) {
				float f3 = f;
				f = x;
				x = f3;
			}
			initQuads(tW, tH, texU, texV, dx, dy, dz, mirror, extra,
				new PositionTextureVertex(f, y, z, 0.0F, 8.0F),
				new PositionTextureVertex(f, f1, z, 8.0F, 8.0F),
				new PositionTextureVertex(x, f1, z, 8.0F, 0.0F),
				new PositionTextureVertex(x, y, f2, 0.0F, 0.0F),
				new PositionTextureVertex(f, y, f2, 0.0F, 8.0F),
				new PositionTextureVertex(f, f1, f2, 8.0F, 8.0F),
				new PositionTextureVertex(x, f1, f2, 8.0F, 0.0F),
				new PositionTextureVertex(x, y, z, 0.0F, 0.0F)
			);
		}

		protected void initQuads(float tW, float tH, float texU, float texV, float dx, float dy, float dz, boolean mirror, boolean extra, PositionTextureVertex vertex,
			PositionTextureVertex vertex1, PositionTextureVertex vertex2, PositionTextureVertex vertex3, PositionTextureVertex vertex4, PositionTextureVertex vertex5,
			PositionTextureVertex vertex6, PositionTextureVertex vertex7) {
			this.quads[0] = new TexturedQuad(texU + dz + dx, texV + dz, texU + dz + dx + dz, texV + dz + dy, tW, tH, mirror, Direction.EAST,
				vertex4, vertex, vertex1, vertex5);
			this.quads[1] = new TexturedQuad(texU, texV + dz, texU + dz, texV + dz + dy, tW, tH, mirror, Direction.WEST,
				vertex7, vertex3, vertex6, vertex2);
			this.quads[2] = new TexturedQuad(texU + dz, texV, texU + dz + dx, texV + dz, tW, tH, mirror, Direction.DOWN,
				vertex4, vertex3, vertex7, vertex);
			this.quads[3] = new TexturedQuad(texU + dz, texV + dz + (dy-1), texU + dz + dx, texV + 1 -1 + dz + dy, tW, tH - 1, mirror, Direction.UP,
				vertex1, vertex2, vertex6, vertex5);
			this.quads[4] = new TexturedQuad(texU + dz, texV + dz, texU + dz + dx, texV + dz + dy, tW, tH, mirror, Direction.NORTH,
				vertex, vertex7, vertex2, vertex1);
		}
	}

	public static class OverlayModelBox extends ModelBox {

		public OverlayModelBox(boolean isLeft, float tW, float tH, float texU, float texV, float x, float y, float z, float dx, float dy, float dz, float delta, boolean mirror) {
			super(tW, tH, texU, texV, x, y, z, dx, dy, dz, delta, mirror, 4, isLeft);
		}

		@Override
		protected void initQuads(float tW, float tH, float texU, float texV, float dx, float dy, float dz, boolean mirror, boolean isLeft, PositionTextureVertex vertex,
			PositionTextureVertex vertex1, PositionTextureVertex vertex2, PositionTextureVertex vertex3, PositionTextureVertex vertex4, PositionTextureVertex vertex5,
			PositionTextureVertex vertex6, PositionTextureVertex vertex7) {
			dx = 4;
//			dy = 5;
			dz = 3;
			if(!isLeft) {
				this.quads[0] = new TexturedQuad(texU + dz + dx, texV + dz, texU + dz + dx + dz, texV + dz + (dy -0.5f), tW, tH, mirror, Direction.EAST,
					vertex4, vertex, vertex1, vertex5);
			} else {
				this.quads[0] = new TexturedQuad(texU, texV + dz, texU + dz, texV + dz + (dy -0.5f), tW, tH, mirror, Direction.WEST,
					vertex7, vertex3, vertex6, vertex2);
			}
			this.quads[1] = new TexturedQuad(texU + dz, texV, texU + dz + dx, texV + dz, tW, tH, mirror, Direction.DOWN,
				vertex4, vertex3, vertex7, vertex);
			this.quads[2] = new TexturedQuad(texU + dz, texV + dz + (dy- 0.5f), texU + dz + dx, texV + 1 -1 + dz + dy, tW, tH - 1, mirror, Direction.UP,
				vertex1, vertex2, vertex6, vertex5);
			this.quads[3] = new TexturedQuad(texU + dz, texV + dz, texU + dz + dx, texV + dz + (dy -0.5f), tW, tH, mirror, Direction.NORTH,
				vertex, vertex7, vertex2, vertex1);
		}
	}

	public static class BreastModelBox extends ModelBox {

		public BreastModelBox(float tW, float tH, float texU, float texV, float x, float y, float z, float dx, float dy, float dz, float delta, boolean mirror) {
			super(tW, tH, texU, texV, x, y, z, dx, dy, dz, delta, mirror);
		}

		@Override
		protected void initQuads(float tW, float tH, float texU, float texV, float dx, float dy, float dz, boolean mirror, boolean extra, PositionTextureVertex vertex,
			PositionTextureVertex vertex1, PositionTextureVertex vertex2, PositionTextureVertex vertex3, PositionTextureVertex vertex4, PositionTextureVertex vertex5,
			PositionTextureVertex vertex6, PositionTextureVertex vertex7) {
			dx = 4;
//			dy = 5;
			dz = 4;
			this.quads[0] = new TexturedQuad(texU + dz + dx, texV + dz, texU + dz + dx + dz, texV + dz + (dy -0.5f), tW, tH, mirror, Direction.EAST,
				vertex4, vertex, vertex1, vertex5);
			this.quads[1] = new TexturedQuad(texU, texV + dz, texU + dz, texV + dz + (dy -0.5f), tW, tH, mirror, Direction.WEST,
				vertex7, vertex3, vertex6, vertex2);
			this.quads[2] = new TexturedQuad(texU + dz, texV, texU + dz + dx, texV + dz, tW, tH, mirror, Direction.DOWN,
				vertex4, vertex3, vertex7, vertex);
			this.quads[3] = new TexturedQuad(texU + dz, texV + dz + (dy -0.5f), texU + dz + dx, texV + 1 -1 + dz + dy, tW, tH - 1, mirror, Direction.UP,
				vertex1, vertex2, vertex6, vertex5);
			this.quads[4] = new TexturedQuad(texU + dz, texV + dz, texU + dz + dx, texV + dz + (dy -0.5f), tW, tH, mirror, Direction.NORTH,
				vertex, vertex7, vertex2, vertex1);
		}
	}

	public static class HipModelBox{

		public WildfireModelRenderer.TexturedQuad[] quads;
		public float posX1;
		public float posY1;
		public float posZ1;
		public float posX2;
		public float posY2;
		public float posZ2;

		protected HipModelBox(float tW, float tH, float texU, float texV, float x, float y, float z, float dx, float dy, float dz, float delta, boolean mirror, int quads, boolean extra, boolean isOuter) {
			z = z - dz;
			this.posX1 = x;
			this.posY1 = y;
			this.posZ1 = z;
			this.posX2 = x + dx;
			this.posY2 = y + dy;
			this.posZ2 = z + dz;
			this.quads = new TexturedQuad[quads]; //8 or 7
			float f = x + dx;
			float f1 = y + dy;
			float f1_2 = y + dy * 0.6f;
			float f2 = z + dz;
			x = x - delta;
			y = y - delta;
			z = z - delta;
			f = f + delta;
			f1 = f1 + delta;
			f2 = f2 + delta;

			initQuads(tW, tH, texU, texV, dx, dy, dz, mirror, extra, isOuter,
					new PositionTextureVertex(f, y, z, 0.0F, 8.0F),//0
					new PositionTextureVertex(f, f1_2, z, 8.0F, 8.0F),//1
					new PositionTextureVertex(x, f1_2, z, 8.0F, 0.0F),//2
					new PositionTextureVertex(x, y, f2, 0.0F, 0.0F),//3
					new PositionTextureVertex(f, y, f2, 0.0F, 8.0F),//4
					new PositionTextureVertex(f, f1_2, f2, 8.0F, 8.0F),//5
					new PositionTextureVertex(x, f1_2, f2, 8.0F, 0.0F),//6
					new PositionTextureVertex(x, y, z, 0.0F, 0.0F),//7
					new PositionTextureVertex(x, f1, z, 8.0F, 0.0F),//8
					new PositionTextureVertex(f, f1, z, 8.0F, 8.0F), //9
					new PositionTextureVertex(f, f1, f2, 8.0F, 8.0F),//10
					new PositionTextureVertex(x, f1, f2, 8.0F, 0.0F)//11
			);
		}
		protected void initQuads(float tW, float tH, float texU, float texV, float dx, float dy, float dz, boolean mirror, boolean extra, boolean isOuter, PositionTextureVertex vertex,
								 PositionTextureVertex vertex1, PositionTextureVertex vertex2, PositionTextureVertex vertex3, PositionTextureVertex vertex4, PositionTextureVertex vertex5,
								 PositionTextureVertex vertex6, PositionTextureVertex vertex7, PositionTextureVertex vertex8, PositionTextureVertex vertex9, PositionTextureVertex vertex10, PositionTextureVertex vertex11) {
			// mirror = false -> right (front view)
			// mirror = true -> left (front view)
			dx = 4;
			dy = 5;
			dz = 2;
			// 30, 27 (right) 26,52 inner mirror = false
			// 34, 27 (left)  10 20 inner mirror = true

			// 30, 43 (right) outer mirror = false
			// 34, 43 (left)  outer mirror = true

//			if(isOuter)  texV +=16;

			int id = 0;
			if(!mirror || (!isOuter && tH != 32)){
				this.quads[id] = new TexturedQuad(texU, texV + dz, texU + dz, texV + dz + dy * 0.6f, tW, tH, false, Direction.WEST,
						vertex4, vertex, vertex1, vertex5);
				id++;
			}

			if(mirror || (!isOuter && tH != 32)){
				if(!mirror){
					this.quads[id] = new TexturedQuad(texU + dx + dz, texV + dz, texU + dz + dx + dz, texV + dz + dy * 0.6f, tW, tH, false, Direction.EAST,
							vertex7, vertex3, vertex6, vertex2);
				}else{
					this.quads[id] = new TexturedQuad(texU + dx + dz -24, texV + dz, texU + dz + dx + dz -24, texV + dz + dy * 0.6f, tW, tH, false, Direction.EAST,
							vertex7, vertex3, vertex6, vertex2);
				}
				id++;
			}

			this.quads[id] = new TexturedQuad(texU + dz, texV, texU + dz + dx, texV + dz, tW, tH, false, Direction.UP,
					vertex7, vertex, vertex4, vertex3); //upper
			id++;

			this.quads[id] = new TexturedQuad(texU + dz, texV + dz, texU + dz + dx, texV + dz + dy * 0.6f, tW, tH, false, Direction.SOUTH,
					vertex3, vertex4, vertex5, vertex6); //rear
			id++;
			if(!isOuter){
				if(tH == 64){
					if(!mirror){
						texU -=4;
						texV +=25;
					}else{
						texU -=24;
						texV -=7;
					}
				}
			}else{
				if(!mirror){
					texU -=20;
					texV +=9;
				}else{
					texU -=24;
					texV -=7;
				}
			}

			if(tH==64){
				if(!mirror || (!isOuter)){
					this.quads[id] = new TexturedQuad(texU, texV, texU + dz, texV + dy * 0.4f, tW, tH, false, Direction.WEST,
							vertex5, vertex1, vertex9, vertex10);
					id++;
				}
				if(mirror || (!isOuter)){
					this.quads[id] = new TexturedQuad(texU + dx + dz -16, texV, texU + dz + dx + dz -16, texV + dy * 0.4f, tW, tH, false, Direction.EAST,
							vertex2, vertex6, vertex11, vertex8);
					id++;
				}

				this.quads[id] = new TexturedQuad(texU + dz, texV + dy * 0.4f, texU + dz + dx, texV + dz + dy * 0.4f, tW, tH, false, Direction.DOWN,
						vertex11, vertex10, vertex9, vertex8); //buttom
				id++;
				this.quads[id] = new TexturedQuad(texU + dz, texV, texU + dz + dx, texV + dy * 0.4f, tW, tH, false, Direction.SOUTH,
						vertex6, vertex5, vertex10, vertex11); //rear
			}
		}
	}

	public record PositionTextureVertex(float x, float y, float z, float texturePositionX, float texturePositionY) {

      public PositionTextureVertex withTexturePosition(float texU, float texV) {
         return new PositionTextureVertex(x, y, z, texU, texV);
      }
   }

   public static class TexturedQuad {
      public final WildfireModelRenderer.PositionTextureVertex[] vertexPositions;
      public final Vec3i normal;

      public TexturedQuad(float u1, float v1, float u2, float v2, float texWidth, float texHeight, boolean mirrorIn, Direction directionIn, PositionTextureVertex... positionsIn) {
		 if (positionsIn.length != 4) {
			 throw new IllegalArgumentException("Wrong number of vertex's. Expected: 4, Received: " + positionsIn.length);
		 }
         this.vertexPositions = positionsIn;
         float f = 0.0F / texWidth;
         float f1 = 0.0F / texHeight;
         positionsIn[0] = positionsIn[0].withTexturePosition(u2 / texWidth - f, v1 / texHeight + f1);
         positionsIn[1] = positionsIn[1].withTexturePosition(u1 / texWidth + f, v1 / texHeight + f1);
         positionsIn[2] = positionsIn[2].withTexturePosition(u1 / texWidth + f, v2 / texHeight - f1);
         positionsIn[3] = positionsIn[3].withTexturePosition(u2 / texWidth - f, v2 / texHeight - f1);
         if (mirrorIn) {
            int i = positionsIn.length;

            for(int j = 0; j < i / 2; ++j) {
				WildfireModelRenderer.PositionTextureVertex vertex = positionsIn[j];
               positionsIn[j] = positionsIn[i - 1 - j]; //0, 1,2,3 -> 3 2 1 0
               positionsIn[i - 1 - j] = vertex;
            }
         }

         this.normal = directionIn.getNormal();
         if (mirrorIn) {
            this.normal.multiply(-1);
         }

      }
   }
}