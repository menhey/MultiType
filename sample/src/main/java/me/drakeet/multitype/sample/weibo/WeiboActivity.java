/*
 * Copyright 2016 drakeet. https://github.com/drakeet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.drakeet.multitype.sample.weibo;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.sample.MenuBaseActivity;
import me.drakeet.multitype.sample.R;
import me.drakeet.multitype.sample.weibo.content.SimpleImage;
import me.drakeet.multitype.sample.weibo.content.SimpleImageViewProvider;
import me.drakeet.multitype.sample.weibo.content.SimpleText;
import me.drakeet.multitype.sample.weibo.content.SimpleTextViewProvider;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;

/**
 * @author drakeet
 */
public class WeiboActivity extends MenuBaseActivity {

    private WeiboAdapter adapter;
    private Items items;

    private static final String JSON_FROM_SERVICE =
        "{\n" +
            "    \"data\":[\n" +
            "        {\n" +
            "            \"content\":{\n" +
            "                \"text\":\"A simple text Weibo: JSON_FROM_SERVICE.\",\n" +
            "                \"content_type\":\"simple_text\"\n" +
            "            },\n" +
            "            \"createTime\":\"Just now\",\n" +
            "            \"user\":{\n" +
            "                \"avatar\":2130903040,\n" +
            "                \"name\":\"drakeet\"\n" +
            "            }\n" +
            "        },\n" +
            "        {\n" +
            "            \"content\":{\n" +
            "                \"resId\":2130837591,\n" +
            "                \"content_type\":\"simple_image\"\n" +
            "            },\n" +
            "            \"createTime\":\"Just now(JSON_FROM_SERVICE)\",\n" +
            "            \"user\":{\n" +
            "                \"avatar\":2130903040,\n" +
            "                \"name\":\"drakeet\"\n" +
            "            }\n" +
            "        }\n" +
            "    ]\n" +
            "}";


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);

        /* WeiboAdapter! */
        adapter = new WeiboAdapter();
        adapter.register(SimpleText.class, new SimpleTextViewProvider());
        adapter.register(SimpleImage.class, new SimpleImageViewProvider());
        recyclerView.setAdapter(adapter);

        items = new Items();

        User user = new User("drakeet", R.mipmap.avatar);
        SimpleText simpleText = new SimpleText("A simple text Weibo: Hello World.");
        SimpleImage simpleImage = new SimpleImage(R.drawable.img_10);
        for (int i = 0; i < 20; i++) {
            items.add(new Weibo(user, simpleText));
            items.add(new Weibo(user, simpleImage));
        }
        adapter.setItems(items);
        adapter.notifyDataSetChanged();

        assertAllRegistered(adapter, items);

        loadRemoteData();
    }


    private void loadRemoteData() {
        RemoteData dataFromParser = GsonProvider.gson.fromJson(
            JSON_FROM_SERVICE, RemoteData.class);
        // Update the items atomically and safely.
        items = new Items(items);
        items.addAll(0, dataFromParser.weibos);
        adapter.setItems(items);
        adapter.notifyDataSetChanged();
    }


    public static class RemoteData {

        @SerializedName("data")
        public List<Weibo> weibos;
    }
}
