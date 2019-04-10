package projeto.suporteentrevista.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import projeto.suporteentrevista.Model.Model;
import projeto.suporteentrevista.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    List<Model> modelList;

    public MyAdapter(Context context, List<Model> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View itemView = LayoutInflater.from(context).inflate(R.layout.view_holder_item,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        //Picasso.get().load(modelList.get(i).getImage_link()).into(myViewHolder.image_view);
        myViewHolder.text_view.setText(modelList.get(i).getText());
        myViewHolder.checkBox.setChecked(modelList.get(i).getIsChecked());

        final int pos = i;
        myViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modelList.get(pos).changeCheck();

            }
        });

    }

    @Override
    public int getItemCount() {

        return modelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        //ImageView image_view;
        TextView text_view;
        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //image_view = (ImageView)itemView.findViewById(R.id.image_view) ;
            text_view = (TextView)itemView.findViewById(R.id.text_view);
            checkBox = (CheckBox)itemView.findViewById(R.id.checkBoxPergunta);

        }
    }
}
