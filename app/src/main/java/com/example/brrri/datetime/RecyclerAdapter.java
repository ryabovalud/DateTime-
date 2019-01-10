package com.example.brrri.datetime;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private String[][] mDataset;
    private Main2Activity thisMain;
    DatabaseHelper sqlHelper;
    SQLiteDatabase db;



    // класс view holder-а с помощью которого мы получаем ссылку на каждый элемент
    // отдельного пункта списка
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // наш пункт состоит из TextView и ImageButton
        public TextView DateTextView, TimeTextView, EventTextView;
        public ImageButton mRemoveButton;

        public ViewHolder(View v) {
            super(v);
            mRemoveButton = (ImageButton) v.findViewById(R.id.ib_remove);
            DateTextView = (TextView) v.findViewById(R.id.tv_recycler_item);
            TimeTextView = (TextView) v.findViewById(R.id.tv_recycler_item2);
            EventTextView = (TextView) v.findViewById(R.id.tv_recycler_item3);
        }
    }

    // Конструктор
    public RecyclerAdapter(String[][] dataset, DatabaseHelper dbHelper, Main2Activity main2Activity) {
        mDataset = dataset;
        sqlHelper = dbHelper;
        thisMain = main2Activity;
    }

    // Создает новые views (вызывается layout manager-ом)
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent,
                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);

        final ViewHolder vh = new ViewHolder(v);
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent myIntent = new Intent(context, MainActivity.class);

                int position = vh.getAdapterPosition();

                myIntent.putExtra("_id",
                        mDataset[position][3]);

                // подключаемся к БД
                SQLiteDatabase base = sqlHelper.getWritableDatabase();

                Cursor query = base.rawQuery("SELECT * FROM BD  WHERE _id = " + mDataset[position][3] +";", null);

                query.moveToFirst();
                Long date_time_fromBD = query.getLong(1);
                String event_fromBD = query.getString(2);
                query.close();
                // закрываем подключение к БД
                sqlHelper.close();

                myIntent.putExtra("date_time_fromBD",
                        date_time_fromBD);
                myIntent.putExtra("event_fromBD",
                        event_fromBD);

                v.getContext().startActivity(myIntent);
            }
        });

        return vh;
    }

    // Заменяет контент отдельного view (вызывается layout manager-ом)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.DateTextView.setText(mDataset[position][0]);
        holder.TimeTextView.setText(mDataset[position][1]);
        holder.EventTextView.setText(mDataset[position][2]);

        // Set a click listener for item remove button
        holder.mRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // подключаемся к БД
                db = sqlHelper.getReadableDatabase();
                // удаляем
                db.delete(sqlHelper.TABLE_NAME, sqlHelper.COLUMN_ID + "=" +mDataset[position][3],null);

                sqlHelper.close();

                NewDataset(mDataset[position][3]);

                notifyItemRemoved(position);

                notifyItemRangeChanged(position,mDataset.length);

            }
        });

    }



    private void NewDataset(String id) {
        String[][] dataset = new String[mDataset.length-1][4];
        int k = 0;
        for (int i=0;i<mDataset.length;i++){
            if (mDataset[i][3]!=id){
                dataset[k][0] = mDataset[i][0];
                dataset[k][1] = mDataset[i][1];
                dataset[k][2] = mDataset[i][2];
                dataset[k][3] = mDataset[i][3];
                k += 1;
            }
        }
        mDataset =  dataset;
    }



    // Возвращает размер данных (вызывается layout manager-ом)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

}

