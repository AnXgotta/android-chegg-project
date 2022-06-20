package homework.chegg.com.chegghomework.ui.adapter

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

@BindingAdapter("srcUrlNewsFeed", requireAll = false)
fun ImageView.bindSrcUrlNewsFeed(
    url: String
) {
    try {
        // could use a default image for failed images
        var t = Glide.with(this)
            .load(url)
            .transform(RoundedCorners(15))
            .placeholder(CircularProgressDrawable(this.context))
            .into(this)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@BindingAdapter("imgDrawable", requireAll = false)
fun ImageView.bindImgDrawable(
    drawable: Drawable
) {
    this.setImageDrawable(drawable)
}

@BindingAdapter(value = ["setAdapter"])
fun RecyclerView.bindRecyclerViewAdapter(adapter: RecyclerView.Adapter<*>) {
    this.run {
        this.setHasFixedSize(true)
        this.adapter = adapter
    }
}
