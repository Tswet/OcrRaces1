package com.gmail.mtswetkov.ocrraces


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.gmail.mtswetkov.ocrraces.model.OcrApi
import com.gmail.mtswetkov.ocrraces.model.Race
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Intent



class MainActivity : AppCompatActivity() {
    private lateinit var raceAdapter : RaceAdapter

    var singleRace : Race?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        raceAdapter = RaceAdapter()
        race_list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        race_list.adapter = raceAdapter


        val retrofit : Retrofit = Retrofit.Builder()
                .baseUrl("http://yar.gks.ru")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        val ocrApi = retrofit.create(OcrApi::class.java)

        ocrApi.getRaces()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ raceAdapter.setRaces(it.item)},
                        {
                            println("test_error" + it.message)
                        })
    }

    inner class RaceAdapter : RecyclerView.Adapter<RaceAdapter.RaceViewHolder>() {

        private val races: MutableList<Race> = mutableListOf()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RaceViewHolder {
            return RaceViewHolder(layoutInflater.inflate(R.layout.race_item, parent, false))
        }

        override fun getItemCount(): Int {
            return races.size
        }

        override fun onBindViewHolder(holder: RaceViewHolder, position: Int) {
            holder.bindModel(races[position])
        }

        fun setRaces(item: List<Race>) {
            races.addAll(item)
            notifyDataSetChanged()
        }

        inner class RaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

            val raceName : TextView = itemView.findViewById(R.id.raceName)
            val raceShortDescription : TextView = itemView.findViewById(R.id.raceShortDescription)
            val raceDate : TextView = itemView.findViewById(R.id.raceDate)
            val raceIcon : ImageView = itemView.findViewById(R.id.raceIcon)
            init {
                itemView.setOnClickListener(this)
            }

            fun bindModel(race: Race) {
                raceName.text = race.name
                raceShortDescription.text = race.shortDescription
                raceDate.text = race.date.toString()//.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                Picasso.get().load(race.icon).into(raceIcon)
            }

            override fun onClick(v: View?) {
                var pos: Int = this.position
                singleRace = races.get(pos)
                val i = Intent(this@MainActivity, ShowSingleRaceActivity::class.java)
                i.putExtra("SHOW_RACE", singleRace)
                startActivity(i)

            }

        }
    }
}
