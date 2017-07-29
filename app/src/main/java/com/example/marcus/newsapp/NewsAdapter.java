package com.example.marcus.newsapp;
import com.example.marcus.newsapp.data.Contract;
import com.example.marcus.newsapp.data.Item;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import java.util.ArrayList;

/**
 * Created by Marcus on 6/26/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ItemHolder> {

    private ArrayList<Item> newsItem;

    private ItemClickListener listener;
    private Cursor cursor;
    private Context context;


    public interface ItemClickListener {

        void onItemClick(Cursor cursor, int clickedItemIndex);
    }

    public NewsAdapter(Cursor cursor, ItemClickListener listener){

        this.cursor = cursor;
        this.listener = listener;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType){
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean parentAttach = false;

        View view = inflater.inflate(R.layout.item, parent, false);
        ItemHolder holder = new ItemHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position){
        holder.bind(position);
    }

    @Override
    public int getItemCount(){
        return cursor.getCount();
    }




    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title, description, time;
        ImageView image;

        ItemHolder(View view){
            super(view);
            title = (TextView)view.findViewById(R.id.itemTitle);
            description = (TextView)view.findViewById(R.id.itemDesc);
            time = (TextView)view.findViewById(R.id.itemTime);
            image = (ImageView)view.findViewById(R.id.itemImg);
            view.setOnClickListener(this);
        }

        public void bind (int pos){
            //Item holderItem = newsItem.get(x);

//            title.setText(holderItem.getTitle());
//            description.setText(holderItem.getDescription());
//            time.setText(holderItem.getPublishedAt());

            cursor.moveToPosition(pos);
            title.setText(cursor.getString(cursor.getColumnIndex(Contract.OBJECT_TABLES.COLUMN_NAME_TITLE)));
            description.setText(cursor.getString(cursor.getColumnIndex(Contract.OBJECT_TABLES.COLUMN_NAME_DESC)));
            time.setText(cursor.getString(cursor.getColumnIndex(Contract.OBJECT_TABLES.COLUMN_NAME_DATE)));

            String url = cursor.getString(cursor.getColumnIndex(Contract.OBJECT_TABLES.COLUMN_NAME_URL));
            Picasso.with(context)
                    .load(url)
                    .into(image);
        }

        @Override
        public void onClick(View v){
            int position = getAdapterPosition();

            listener.onItemClick(cursor, position);
        }

    }
}