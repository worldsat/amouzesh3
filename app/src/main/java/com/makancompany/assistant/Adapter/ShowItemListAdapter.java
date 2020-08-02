package com.makancompany.assistant.Adapter;


import android.animation.Animator;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.makancompany.assistant.Domain.Question;
import com.makancompany.assistant.Interface.OnClick;
import com.makancompany.assistant.R;

import java.util.ArrayList;

public class ShowItemListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Question> array_object;
    private int position;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            row_index = position;
            notifyDataSetChanged();
        }
    };
    private static final int Empty = 0;
    private static final int ManyQuestion = 1;
    private static final int Question1Answer1 = 2;

    private Context context;
    int delayAnimate = 300; //global variable
    int delayAnimate2 = 0; //global variable
    private boolean exit = false;
    private boolean endStart = false;
    private boolean endExit = false;
    private int row_index = -1;
    private OnClick onClick;

    private int setRowColor = 0;

    public ShowItemListAdapter(ArrayList<Question> result, OnClick onClick) {

        this.array_object = result;
        this.onClick = onClick;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_question, parent, false);
        context = view.getContext();
        if (viewType == ManyQuestion) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_question, parent, false);
            view.setOnClickListener(mOnClickListener);
            return new ManyQuestionViewHolder(view);
        } else if (viewType == Question1Answer1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_question1_answer1, parent, false);
            return new Question1Answer1ViewHolder(view);
        }

        return new Question1Answer1ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ManyQuestionViewHolder) {
            initManyQuestion((ManyQuestionViewHolder) holder, position);
        } else if (holder instanceof Question1Answer1ViewHolder) {
            initQuestion1Answer1((Question1Answer1ViewHolder) holder, position);
        }
    }

    private void initManyQuestion(ManyQuestionViewHolder holder, int position) {


        holder.title.setText(array_object.get(position).getQuestion());
        setbackground(position, holder.card, holder.icon_background, holder.title);

        holder.card.setVisibility(View.INVISIBLE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(holder.itemView);

            }
        });
        animate(holder.itemView, holder.card, position);


    }


    private void initQuestion1Answer1(Question1Answer1ViewHolder holder, int position) {

        holder.title.setText(array_object.get(position).getQuestion());
//        setbackground(position, holder.card, holder.icon_background, holder.title);
        holder.card.setVisibility(View.INVISIBLE);
//
        animate(holder.itemView, holder.card, position);
        String[] fruits = {"تلفن همراه", "کامپویتر رومیزی", "لب تاپ", "دوربین دیجیتال", "فلش مموری", "تلویزیون"};

        //Creating the instance of ArrayAdapter containing list of fruit names
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (context, R.layout.spinner_item_blue, fruits);
        //Getting the instance of AutoCompleteTextView

        holder.autoCompleteTextView.setThreshold(1);//will start working from first character
        holder.autoCompleteTextView.setAdapter(adapter);//setting thتdapter data into the AutoCompleteTextView
        holder.autoCompleteTextView.setTextColor(Color.RED);

        holder.autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                click(holder.itemView);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(holder.itemView);
            }
        });

    }

    private void click(View itemView) {
        if (!endExit) {
            itemView.setVisibility(View.VISIBLE);
            exit = true;
            notifyDataSetChanged();
        }
    }

    private void animate(View itemView, ConstraintLayout card, int position) {
        if (exit) {
            itemView.setVisibility(View.VISIBLE);

            setAnimationOut(card);

        } else {
            if (!endStart) {
                setAnimationIn(card);
            } else {
                itemView.setVisibility(View.VISIBLE);
            }
        }
        if (array_object.size() == (position + 1)) {
            endStart = true;
        }
    }

    @Override
    public int getItemCount() {
        return array_object == null ? 0 : array_object.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {

        if (array_object.get(position).getType().equals("1")) {
            return ManyQuestion;
        } else if (array_object.get(position).getType().equals("2")) {
            return Question1Answer1;
        } else {
            return Empty;
        }

    }


    class ManyQuestionViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ConstraintLayout card, icon_background;
        ImageView icon;


        private ManyQuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            card = itemView.findViewById(R.id.item);
            icon_background = itemView.findViewById(R.id.icon_background);
            icon = itemView.findViewById(R.id.icon);

        }
    }

    class Question1Answer1ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ConstraintLayout card, icon_background;
        ImageView icon;
        AutoCompleteTextView autoCompleteTextView;

        private Question1Answer1ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            card = itemView.findViewById(R.id.item2);
            icon_background = itemView.findViewById(R.id.icon_background);
            icon = itemView.findViewById(R.id.icon);
            autoCompleteTextView = itemView.findViewById(R.id.autoCompleteTextView);
        }
    }

    private void setAnimationIn(final View view) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
                if (view != null) {
                    view.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.ZoomInUp)
                            .duration(1300)
                            .repeat(1)
                            .playOn(view);
                }
            }
        }, delayAnimate);
        delayAnimate += 150;
    }

    private void setAnimationOut(final View view) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (view != null) {
                    view.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.ZoomOutUp)
                            .duration(1200)
                            .repeat(1)
                            .onStart(new YoYo.AnimatorCallback() {
                                @Override
                                public void call(Animator animator) {
                                    endExit = true;
                                }
                            })
                            .onEnd(new YoYo.AnimatorCallback() {
                                @Override
                                public void call(Animator animator) {
                                    if (exit) {
                                        onClick.clicked(array_object.get(0).getNameQuestion());
                                        exit = false;
                                    }
                                }
                            })
                            .playOn(view);
                }
            }
        }, delayAnimate2);
        delayAnimate2 += 150;
    }

    private void setbackground(int position, ConstraintLayout item, ConstraintLayout icon_background, TextView title) {
        if (setRowColor == 4) {
            setRowColor = 0;
        }
        setRowColor++;
        switch (setRowColor) {

            case 0: {
                item.setBackgroundResource(R.drawable.background_circle_green);
                icon_background.setBackgroundResource(R.drawable.background_circle_green2);
                icon_background.setVisibility(View.GONE);

//                title.setTextColor(context.getResources().getColor(R.color.grey_800));
                break;
            }
            case 1: {

                item.setBackgroundResource(R.drawable.background_circle);
                icon_background.setBackgroundResource(R.drawable.background_circle_red2);
                title.setTextColor(context.getResources().getColor(R.color.grey_800));
                break;
            }
            case 2: {
                item.setBackgroundResource(R.drawable.background_circle);
                icon_background.setBackgroundResource(R.drawable.background_circle_blue2);
                title.setTextColor(context.getResources().getColor(R.color.grey_800));
                break;
            }
            case 3: {
                item.setBackgroundResource(R.drawable.background_circle);
                icon_background.setBackgroundResource(R.drawable.background_circle_orange2);
                title.setTextColor(context.getResources().getColor(R.color.grey_800));
                break;
            }
            case 4: {

                item.setBackgroundResource(R.drawable.background_circle);
                icon_background.setBackgroundResource(R.drawable.background_circle_green2);
                title.setTextColor(context.getResources().getColor(R.color.grey_800));
                break;
            }
        }
    }


}
