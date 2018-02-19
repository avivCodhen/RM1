@file:JvmName("AnimationUtil")

package com.strongest.savingdata.Animations

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import com.strongest.savingdata.Adapters.MyExpandableAdapter

/**
 * Created by Cohen on 2/13/2018.
 */

class MyAnimator {

    companion object {

        @JvmStatic fun fadeInOut(views: Array<View>) {
            for (view in views) {
                view.animate().alpha(0f).setListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }

                    override fun onAnimationStart(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        view.animate().alpha(1f).start()
                    }
                }).start()
            }
        }
    }


}