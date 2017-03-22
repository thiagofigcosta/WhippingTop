package br.cefetmg.move2play.whippingtop.util;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import br.cefetmg.move2play.whippingtop.game.Obstacle;
import br.cefetmg.move2play.whippingtop.game.Track;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ModelCreator {
    
    
    
    
    public static ModelInstance createSkyModel(Texture skyTex){
        Material skyMaterial = new Material(TextureAttribute.createDiffuse(skyTex));
        ModelBuilder modelBuilder = new ModelBuilder();
        return new ModelInstance(modelBuilder.createSphere(-300, -300, -300, 20, 20, skyMaterial, VertexAttributes.Usage.Position|VertexAttributes.Usage.Normal|VertexAttributes.Usage.TextureCoordinates));
    }
    
    private static double[] createTrackModel_createFloor(double size,double pos, double Xoffset, double Zoffset, int widthReducer,float depth,boolean defaultUVrange,Material mat,ModelBuilder modelBuilder){
        if(size>0){
            MeshPartBuilder base = modelBuilder.part("floor",GL20.GL_TRIANGLES,VertexAttributes.Usage.Position|VertexAttributes.Usage.Normal|VertexAttributes.Usage.TextureCoordinates, mat);
            if(!defaultUVrange)
                base.setUVRange(0, 0, (float) (size/23), 1);
            base.rect(  (float)(-Track.TRACKWIDTH/widthReducer+Xoffset),depth,(float)(pos+Zoffset), 
                        (float)(-Track.TRACKWIDTH/widthReducer+Xoffset),depth,(float)(size+pos+Zoffset), 
                        (float)(Track.TRACKWIDTH/widthReducer+Xoffset),depth,(float)(size+pos+Zoffset), 
                        (float)(Track.TRACKWIDTH/widthReducer+Xoffset),depth,(float)(pos+Zoffset),               0, 1, 0);
        }
        pos+=size;
        size=0;
        double[] out=new double[2];
        out[0]=size;
        out[1]=pos;
        return out;
    }
    public static ModelInstance createTrackModel(Track track,double Xoffset,double Zoffset,Texture floorText, Texture arrivalText, Texture[] bridges,Texture speedUpText){
        floorText.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);
        Material trackMaterial = new Material(TextureAttribute.createDiffuse(floorText));
        Texture bridgeText = bridges[Util.genInt(bridges.length)];
        Material obstacleMaterial = new Material(TextureAttribute.createDiffuse(bridgeText));
        obstacleMaterial.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
        double trackRealSize=0;
        double movedPosZ=0;
        int lastBridge=0;
        double[] size;
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        modelBuilder.node().id = "track";
        MeshPartBuilder obstacles;
        for(int i=0;i<track.getFullSize();i++){           
            if(track.obstacleTypeAt(i)==Obstacle.BRIDGE){
                if(i-lastBridge>1){
                    bridgeText = bridges[Util.genInt(bridges.length)];
                    obstacleMaterial = new Material(TextureAttribute.createDiffuse(bridgeText));
                    obstacleMaterial.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
                }
                lastBridge=i;
                size=createTrackModel_createFloor(trackRealSize,movedPosZ,Xoffset,Zoffset,2,0,false,trackMaterial,modelBuilder);
                trackRealSize=size[0];
                movedPosZ=size[1];
                size=createTrackModel_createFloor(track.sizeAt(i),movedPosZ,Xoffset,Zoffset,8,0,true,obstacleMaterial,modelBuilder);
                trackRealSize=size[0];
                movedPosZ=size[1];
            }else{
                if(track.obstacleTypeAt(i)==Obstacle.SPEEDUP){
                    obstacleMaterial = new Material(TextureAttribute.createDiffuse(speedUpText));
                    createTrackModel_createFloor(track.sizeAt(i),movedPosZ+trackRealSize,Xoffset,Zoffset,2,0.1f,true,obstacleMaterial,modelBuilder);
                }else if(track.obstacleTypeAt(i)==Obstacle.ARRIVAL){
                    obstacleMaterial = new Material(TextureAttribute.createDiffuse(arrivalText));
                    createTrackModel_createFloor(track.sizeAt(i),movedPosZ+trackRealSize,Xoffset,Zoffset,2,0.1f,true,obstacleMaterial,modelBuilder);
                }                
                trackRealSize+=track.sizeAt(i);
            }
        }  
        createTrackModel_createFloor(trackRealSize,movedPosZ,Xoffset,Zoffset,2,0,false,trackMaterial,modelBuilder);
        return new ModelInstance(modelBuilder.end());
    }
    
    public static List<ModelInstance> createTrackObjs(Track track,double Xoffset,double Zoffset,Model grassModel,Model[] cones){
        List<ModelInstance> trackObjs=new ArrayList();
        float currentSize=0;
        for(int i=0;i<track.getFullSize();i++){
            if(track.obstacleTypeAt(i)==Obstacle.GRASS){
                ModelInstance tmp=new ModelInstance(grassModel);
                tmp.transform.setToTranslation((float)Xoffset, 0, currentSize);
                trackObjs.add(tmp);
            }else if(track.obstacleTypeAt(i)==Obstacle.CONE){
                int nCones=(int)Math.round(track.difficultyAt(i))+2;
                float base=11.5f;
                Random rand = new Random();
                for(int n=0;n<nCones;n++){
                    Model coneModel=cones[Util.genInt(cones.length)];
                    ModelInstance tmp=new ModelInstance(coneModel);
                    float randX=rand.nextFloat()*base*0.71f+(1.3f*(rand.nextFloat()+0.13f));
                    if(n%2==0){
                        tmp.transform.setToTranslation((float)(-randX+Xoffset), 0, currentSize+10+(rand.nextFloat()+0.3f)*randX*n);
                    }else{
                        tmp.transform.setToTranslation((float)(+randX+Xoffset), 0, currentSize+10+(rand.nextFloat()+0.3f)*randX*n);
                    }
                    trackObjs.add(tmp);
                }  
            }
            currentSize+=track.sizeAt(i);
        }
        return trackObjs;
    }
}
