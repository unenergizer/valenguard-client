package com.valenguard.client;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.valenguard.client.entities.Entity;
import com.valenguard.client.network.Client;
import com.valenguard.client.network.ClientEventBus;
import com.valenguard.client.util.AttachableCamera;
import com.valenguard.client.util.Consumer;
import com.valenguard.client.util.Controller;

/**
 * Created by unene on 12/20/2017.
 */

public class Valenguard extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;

    private Entity player;
    private AttachableCamera camera;

    // TODO: MOVE TO GAME SCREEN
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("link.png");

        Client client = new Client();
        final Valenguard val = this;
        client.connect("localhost", (short) 3407, new Consumer<ClientEventBus>() {
            @Override
            public void accept(ClientEventBus clientEventBus) {

            }
        });

        camera = new AttachableCamera(Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT);
        player = new Entity(0, 0);
        camera.attachEntity(player);

        // TODO: MOVE TO GAME SCREEN
        map = new TmxMapLoader().load("maps/maintown.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        Controller inputProcessor = new Controller(player);
        Gdx.input.setInputProcessor(inputProcessor);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // TODO: MOVE TO GAME SCREEN
        mapRenderer.setView(camera);
        mapRenderer.render();

        batch.begin();
        player.draw(batch, img);
        //batch.draw(new Texture("badlogic.jpg"), 50, 50);
        batch.end();

    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }
}
