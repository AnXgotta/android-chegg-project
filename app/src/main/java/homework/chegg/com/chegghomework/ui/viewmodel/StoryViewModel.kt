package homework.chegg.com.chegghomework.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import homework.chegg.com.chegghomework.data.entities.StoryItem
import homework.chegg.com.chegghomework.data.repository.StoryRepository
import homework.chegg.com.chegghomework.ui.extensions.subscribeWithTrace
import homework.chegg.com.chegghomework.util.DataResource
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class StoryViewModel @Inject constructor(
    application: Application,
    private val storyRepository: StoryRepository
) : AndroidViewModel(application) {

    private val disposables: CompositeDisposable = CompositeDisposable()

    val storiesLiveData = MutableLiveData<DataResource<List<StoryItem>>>(DataResource.loading())

    fun getStories() {
        storiesLiveData.postValue(DataResource.loading())
        storyRepository.getStories()
            .observeOn(Schedulers.io())
            .subscribeWithTrace(
                onSuccess = { data ->
                    if (data.isNullOrEmpty()) {
                        storiesLiveData.postValue(DataResource.empty())
                    } else {
                        storiesLiveData.postValue(DataResource.success(data))
                    }
                },
                onError = {
                    // could create custom errors to handle specific cases
                    // generic placeholder for now
                    storiesLiveData.postValue(DataResource.error(it.message ?: "An error occurred"))
                }
            ).addTo(disposables)
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}
