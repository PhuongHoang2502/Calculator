package bk.bosch.calculator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ButtonViewHolder> {
    private List<MyButton> buttons;
    private OnButtonClickListener clickListener;

    public interface OnButtonClickListener{
        void onButtonClick(String buttonText, MyButton.ButtonType buttonType);
    }

    public MyAdapter(List<MyButton> buttons, OnButtonClickListener clickListener) {
        this.buttons = buttons;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ButtonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_layout, parent, false);
        return new ButtonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ButtonViewHolder holder, int position) {
        MyButton button = buttons.get(position);
        holder.bind(button);
    }

    @Override
    public int getItemCount() {
        return buttons.size();
    }

    public class ButtonViewHolder extends RecyclerView.ViewHolder{
        private MaterialButton button;

        public ButtonViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.button);
        }
        public void bind(MyButton myButton){
            button.setText(myButton.getText());
            button.setBackgroundTintList(ContextCompat.getColorStateList(button.getContext(), myButton.getBackgroundColor()));
            button.setTextColor(ContextCompat.getColor(button.getContext(), myButton.getTextColor()));
            //AC button
            if(myButton.getText().length() > 1){
                button.setTextSize(20f);
            }else{
                button.setTextSize(32f);
            }

            button.setOnClickListener(view -> {
                if(clickListener != null){
                    clickListener.onButtonClick(myButton.getText(), myButton.getType());
                }
            });
        }
    }

//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        MyButton button = getItem(position);
//
//        MyViewHolder myViewHolder;
//
//        if(convertView == null){
//            myViewHolder = new MyViewHolder();
//            LayoutInflater inflater = LayoutInflater.from(getContext());
//            convertView = inflater.inflate(R.layout.grid_item_layout, parent, false);
//            myViewHolder.button = (MaterialButton) convertView.findViewById(R.id.button);
//
//            convertView.setTag(myViewHolder);
//        }else{
//            myViewHolder = (MyViewHolder) convertView.getTag();
//        }
//
////        myViewHolder.button.setText(myButtons.getText());
//        return convertView;
//    }
}
