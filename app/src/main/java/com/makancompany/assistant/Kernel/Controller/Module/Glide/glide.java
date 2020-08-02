package com.makancompany.assistant.Kernel.Controller.Module.Glide;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.makancompany.assistant.Kernel.Controller.Module.Glide.Interface.glide_interface;


public class glide implements glide_interface {
    private Context context;


    public glide(Context context) {
        this.context = context;

    }


    @Override
    public void Loadimage(int url, ImageView view) {

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions
                .transforms(new CenterCrop());
//         .transforms(new CenterCrop(), new RoundedCorners(8));
//                .error(R.mipmap.error)
//                .override(100, 100)
//                .placeholder(R.mipmap.error);


        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(view);

    }
}
