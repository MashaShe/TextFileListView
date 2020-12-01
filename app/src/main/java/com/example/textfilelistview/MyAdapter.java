package com.example.textfilelistview;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends BaseAdapter {
    private List<MyData> items;
    int position;
    // LayoutInflater – класс, который из
    // layout-файла создает View-элемент.
    private LayoutInflater inflater;

    // Конструктор, в который передается контекст
    // для создания контролов из XML. И первоначальный список элементов.
    MyAdapter(Context context, List<MyData> items) {
        if (items == null) {
            this.items = new ArrayList<>();
        } else {
            this.items = items;
        }
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    void addItem(MyData item) {
        this.items.add(item);
        notifyDataSetChanged();
    }

    void removeItem(int position) {
        items.remove(position);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public MyData getItem(int position) {
        if (position < items.size()) {
            return items.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.list_item, parent, false);
        }

        MyData itemData = items.get(position);


        TextView title = view.findViewById(R.id.textViewHeader);
        TextView subtitle = view.findViewById(R.id.textViewText);
        TextView author = view.findViewById(R.id.textViewAuthor);
        final Button button = view.findViewById(R.id.buttonDel);

        title.setText(itemData.getHeader());
        subtitle.setText(itemData.getText());
        author.setText(itemData.getAuthor());
        //  button.setTag(String.valueOf(position));
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeItem(position);
                String line = null;
                StringBuilder stringBuilder = new StringBuilder();
                String ls = System.getProperty("line.separator");

                for (int i = 0; i < getCount(); i++) {
                    line = getItem(i).toString();
                    stringBuilder.append(line);
                    stringBuilder.append(ls);
                }

                String state = Environment.getExternalStorageState();
                if (Environment.MEDIA_MOUNTED.equals(state)) {
                    File file = new File(Environment
                            .getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_DOCUMENTS),
                            "longText.txt");

                    try (FileWriter fw = new FileWriter(file);) {
                        fw.write(stringBuilder.toString());
                    } catch (Exception e) {
                        System.out.println(e);
                    }


                }
            }
        });
        button.setFocusable(false);
        return view;
    }
}
