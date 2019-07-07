package co.com.geo.userlist.data.mappers

/**
 * Created by costular on 17/03/2018.
 */
interface Mapper<in R, out T> {

    fun transform(input: R): T
    fun transformList(inputList: List<R>): List<T>

}