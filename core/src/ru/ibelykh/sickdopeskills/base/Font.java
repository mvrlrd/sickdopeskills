package ru.ibelykh.sickdopeskills.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class Font extends BitmapFont {

//    public Font(String fontFile, String imageFile) {
//        super(Gdx.files.internal(fontFile), Gdx.files.internal(imageFile), true);
//        getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
//    }

//    public void setFontSize(float size){
//
//        getData().setScale(size);
//        System.out.println(getCapHeight()+" capcapcap");
//        System.out.println(size/getCapHeight()+"    eeeeeeeeeeeeeeeeeeeee");
//
//    }


//    @Override
//    public GlyphLayout draw(Batch batch, CharSequence str, float x, float y) {
//        return super.draw(batch, str, x, y);
//    }


        public Font(String fontFile, String imageFile) {
        super(Gdx.files.internal(fontFile), Gdx.files.internal(imageFile), false,false);
        getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    public void setFontSize(float size) {
        getData().setScale(size / getCapHeight());
        System.out.println(getCapHeight()+" capcapcap");
 System.out.println(size/getCapHeight()+"    eeeeeeeeeeeeeeeeeeeee");
    }
//}
    public GlyphLayout draw(Batch batch, CharSequence str, float x, float y, int align){
        return super.draw(batch,str,x,y,0f,align,false);
    }
}