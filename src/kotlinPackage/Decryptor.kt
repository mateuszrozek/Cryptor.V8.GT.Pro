package kotlinPackage

/**
 * Created by lenovo on 2019-04-15.
 */
class Decryptor(var encryptedText: String, var key: String) {

    val NUMBER_OF_COLUMNS = 4
    val NUMBER_OF_BLOCK_ROWS = 6
    val NUMBER_OF_KEY_ROWS = 2
    val BLOCK_LENGTH = NUMBER_OF_COLUMNS * NUMBER_OF_BLOCK_ROWS

    fun decrypt() : String {

        val block = Matrix(NUMBER_OF_COLUMNS, NUMBER_OF_BLOCK_ROWS, encryptedText)
        val key = Matrix(NUMBER_OF_COLUMNS, NUMBER_OF_KEY_ROWS, key)

        val keyArray = key.arrayASCII

        println("Encrypted block")
        block.printCharArray()

        println("Known key")
        key.printCharArray()

        val translatedBlock = translateBlock(block.array)
        println("Translated block")
        printIntArray(translatedBlock)

        val permutedBlock = permuteBlock(translatedBlock)
        println("Permuted block")
        printIntArray(permutedBlock)

        reverseSecondRow(keyArray)
        println("Reversed key second row")
        printIntArray(keyArray)

        val substractedBlock = substractBlock(keyArray, permutedBlock)
        println("Substracted block")
        printIntArray(substractedBlock)

        val decryptedMatrix = translateBlock(substractedBlock)
        println("Decrypted block")
        printCharArray(decryptedMatrix)

        val blockAsString = flattenBlock(decryptedMatrix)
        println("Block as list")
        println(blockAsString)



        return blockAsString
    }

    private fun  translateBlock(block: Array<Array<Char>>): Array<Array<Int>> {

        val result = Array(NUMBER_OF_BLOCK_ROWS, { Array(NUMBER_OF_COLUMNS, {0}) })
        for (i in 0..result.size-1){
            for (j in 0..result[0].size-1){
                result[i][j] = block[i][j].toInt()

            }
        }
        return result
    }

    private fun permuteBlock(block: Array<Array<Int>>): Array<Array<Int>> {

        for (i in 0..block.size-1){
            val temp1 = block[i][1]
            val temp2 = block[i][2]
            block[i][1] = temp2
            block[i][2] = temp1
        }
        return block
    }

    fun substractBlock(key: Array<Array<Int>>, block: Array<Array<Int>>): Array<Array<Int>> {

        for (i in 0..block.size-1){
            if (i % 2 == 0){
                substractIntsInRows(block[i], key[0])
            } else{
                substractIntsInRows(block[i], key[1])
            }
        }
        return block
    }

    private fun  substractIntsInRows(blockRow: Array<Int>, keyRow: Array<Int>) {
        for (i in 0..blockRow.size-1){
            blockRow[i] = blockRow[i] - keyRow[i]
        }
    }

    fun reverseSecondRow(array: Array<Array<Int>>){
        val reversedSecondRow = Array(NUMBER_OF_COLUMNS, {0})

        var counter = array[0].size-1
        for (i in 0..reversedSecondRow.size-1){
            reversedSecondRow[i] = array[1][counter]
            counter--
        }
        array[1] = reversedSecondRow
    }

    private fun translateBlock(block: Array<Array<Int>>): Array<Array<Char>> {

        val result = Array(NUMBER_OF_BLOCK_ROWS, { Array(NUMBER_OF_COLUMNS, {'a'}) })
        for (i in 0..result.size-1){
            for (j in 0..result[0].size-1){
                result[i][j] = block[i][j].toChar()

            }
        }
        return result
    }

    private fun  flattenBlock(block: Array<Array<Char>>): String {

        val a = block.flatMap {it.asIterable()}.toCharArray()
        return String(a)
    }


    fun printIntArray(array: Array<Array<Int>>){
        array.forEach {
            row -> printArrayRow(row)
        }
        println()
    }

    private fun printArrayRow(row: Array<Int>) {
        row.forEach{i -> printElement(i)}
        println()
    }
    private fun printElement(i: Int){
        print(i.toString() + "|")
    }

    fun printCharArray(array: Array<Array<Char>>){
        array.forEach {
            row -> printArrayRow(row)
        }
        println()
    }

    private fun printArrayRow(row: Array<Char>) {
        row.forEach{c -> printElement(c)}
        println()
    }
    private fun printElement(c: Char){
        print(c.toString() + "|")
    }

}