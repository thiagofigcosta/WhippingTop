package br.cefetmg.move2play.whippingtop.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationSprite {
    private int spriteSheetRows;
    private int spriteSheetCols;
    private Texture spriteSheet;
    private float stateTime;
    private float interval;
    private float posX,posY;
    private float scaleX,scaleY;
    private Animation<TextureRegion> animation;

    public AnimationSprite(Texture spriteSheet,float posx,float posy,float scaleX,float scaleY,float interval,int spriteSheetRows, int spriteSheetCols) {
        this.spriteSheetRows = spriteSheetRows;
        this.spriteSheetCols = spriteSheetCols;
        this.spriteSheet = spriteSheet;
        this.interval=interval;
        this.posX=posx;
        this.posY=posy;
        this.scaleX=scaleX;
        this.scaleY=scaleY;
        stateTime=0;
        TextureRegion[][]tmp=TextureRegion.split(spriteSheet,spriteSheet.getWidth()/spriteSheetCols,spriteSheet.getHeight()/spriteSheetRows);
        TextureRegion[]textureCoords=new TextureRegion[spriteSheetRows*spriteSheetCols];
        int index=0;
        for(int i=0;i<spriteSheetRows;i++){
            for(int j=0;j<spriteSheetCols;j++){
                textureCoords[index++]=tmp[i][j];
            }
        }
        animation = new Animation<TextureRegion>(interval,textureCoords);
    }

    public float getInterval() {
        return interval;
    }
    
    public void draw(SpriteBatch batch){
        stateTime+=Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame=animation.getKeyFrame(stateTime, true);
        batch.begin();
        batch.draw(currentFrame,posX,posY,currentFrame.getRegionWidth()*scaleX, currentFrame.getRegionHeight()*scaleY);
        batch.end();
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posx) {
        this.posX = posx;
    }

    public float getPosY() {
        return posY;
    }

    public float getScaleX() {
        return scaleX;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }
    
    public void setPosY(float posy) {
        this.posY = posy;
    }

    public int getSpriteSheetRows() {
        return spriteSheetRows;
    }

    public void setSpriteSheetRows(int spriteSheetRows) {
        this.spriteSheetRows = spriteSheetRows;
    }

    public int getSpriteSheetCols() {
        return spriteSheetCols;
    }

    public void setSpriteSheetCols(int spriteSheetCols) {
        this.spriteSheetCols = spriteSheetCols;
    }

    public Texture getSpriteSheet() {
        return spriteSheet;
    }

    public void setSpriteSheet(Texture spriteSheet) {
        this.spriteSheet = spriteSheet;
    }
    
    
        
}
