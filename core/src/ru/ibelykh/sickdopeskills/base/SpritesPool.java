package ru.ibelykh.sickdopeskills.base;



import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public abstract class SpritesPool <T extends  Sprite> {

    //список активных объектов
    protected List<T> activeObjects = new ArrayList<T>();

    //список свободных объектов
    protected List<T> freeObjects = new ArrayList<T>();

    //проверяет есть ли нужный объект в пуле. если есть - вытаскивает его в активный пул и удаляет из фри. если нет то создает
    public T obtain(){
        T object;
        if (freeObjects.isEmpty()){
            object = newObject();
        }
        else {
            object = freeObjects.remove(freeObjects.size() -1 );

        }
        activeObjects.add(object);

//        System.out.println(this.getClass().getSimpleName()+ "  active/free: "+ activeObjects.size()+" / "+ freeObjects.size());
        return  object;
    }

    protected abstract T newObject();

    public void updateActiveSprites(float delta) {
        for (int i = 0; i < activeObjects.size(); i++) {
            Sprite sprite = activeObjects.get(i);
            if (!sprite.isDestroyed()) {
                sprite.update(delta);
            }
        }
    }

    public void drawActiveSprites(SpriteBatch batch) {
        for (int i = 0; i < activeObjects.size(); i++) {
            Sprite sprite = activeObjects.get(i);
            if (!sprite.isDestroyed()) {
                sprite.draw(batch);
            }
        }
    }

    private void free(T object){
        if (activeObjects.remove(object)){
            freeObjects.add(object);
//            System.out.println("active/free: "+ activeObjects.size()+" / "+ freeObjects.size());
        }
    }

    public void dispose(){
        activeObjects.clear();
        freeObjects.clear();
    }

    public List<T> getActiveObjects() {
        return activeObjects;
    }

    public void freeAllDestroyedActiveSprites() {
        for (int i = 0; i < activeObjects.size(); i++) {
            T sprite = activeObjects.get(i);
            if (sprite.isDestroyed()) {
                free(sprite);
                i-- ;
                sprite.setDestroyed(false);
            }
        }
    }
    public void freeAllActiveObjects(){
        freeObjects.addAll(activeObjects);
        activeObjects.clear();
    }
}
