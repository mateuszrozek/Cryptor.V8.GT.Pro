package kotlinPackage

class Matrix constructor(columns: Int, rows: Int, text: String){

    var array = Array(rows, { Array(columns, {'a'}) })
    var arrayASCII = Array(rows, { Array(columns, {0}) })


    init {

        var point = 0
        for (i in 0..rows-1){
            val tempArray = text.substring(point, point + columns).toCharArray()
            array[i] = tempArray.toTypedArray()
            point += columns
        }
        arrayASCII = convertToASCII(array)
    }



    fun convertToASCII(array: Array<Array<Char>>): Array<Array<Int>> {
        val arrayASCII = Array(array.size, { Array(array[0].size, {0}) })

        for (i in 0..array.size-1){

            arrayASCII[i] = Array(array[0].size, {0})
            for (j in 0..array[i].size -1 ){

                arrayASCII[i][j] = array[i][j].toInt()
            }
        }
        return arrayASCII
    }

    fun printCharArray(){
        array.forEach {
            row -> printCharRow(row)
        }
        println()
    }

    private fun printCharRow(row: Array<Char>) {
        row.forEach{char->printChar(char)}
        println()
    }

    private fun printChar(char: Char){
        print(char.toString() + "|")
    }

    fun printIntArray(){
        arrayASCII.forEach {
            row -> printArrayRow(row)
        }
        println()
    }

    private fun printArrayRow(row: Array<Int>) {
        row.forEach{i->printInt(i)}
        println()
    }

    private fun printInt(i: Int){
        print(i.toString() + "|")
    }

}