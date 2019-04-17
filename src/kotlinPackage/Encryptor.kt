package kotlinPackage

class Encryptor(var text: String, var validatedKey: String) {

    val NUMBER_OF_COLUMNS = 4
    val NUMBER_OF_BLOCK_ROWS = 6
    val NUMBER_OF_KEY_ROWS = 2
    val BLOCK_LENGTH = NUMBER_OF_COLUMNS * NUMBER_OF_BLOCK_ROWS

    fun encrypt(): String {

        val processedKey = Matrix(NUMBER_OF_COLUMNS, NUMBER_OF_KEY_ROWS, validatedKey)
        val processedBlock = Matrix(NUMBER_OF_COLUMNS, NUMBER_OF_BLOCK_ROWS, text)

        println("Key as chars")
        processedKey.printCharArray()
        println("Block as char")
        processedBlock.printCharArray()

        println("Key as ints")
        processedKey.printIntArray()
        println("Block as ints")
        processedBlock.printIntArray()


        val key = processedKey.arrayASCII
        val block = processedBlock.arrayASCII

        reverseSecondRow(key)
        println("Reversed key second row")
        printIntArray(key)


        val summarisedBlock = summarise(key, block)
        println("Summarised block and key")
        printIntArray(summarisedBlock)

        val permutedBlock = permuteBlock(block)
        println("Permutated block")
        printIntArray(permutedBlock)

        val reversedBlock = reverseBlock(permutedBlock)
        println("Reversed block")
        printIntArray(reversedBlock)

        val translatedBlock = translateBlock(permutedBlock)
        println("Translated block")
        printCharArray(translatedBlock)

        val blockAsString = flattenBlock(translatedBlock)
        println("Block as list")
        println(blockAsString)
        println()
        println()


        return blockAsString
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

    fun summarise(key: Array<Array<Int>>, block: Array<Array<Int>>): Array<Array<Int>> {

        for (i in 0..block.size-1){
            if (i % 2 == 0){
                addIntsInRows(block[i], key[0])
            } else{
                addIntsInRows(block[i], key[1])
            }
        }
        return block
    }

    private fun  addIntsInRows(blockRow: Array<Int>, keyRow: Array<Int>) {
        for (i in 0..blockRow.size-1){
            blockRow[i] = blockRow[i] + keyRow[i]
        }
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

    private fun reverseBlock(block: Array<Array<Int>>): Array<Array<Int>> {
        val result = Array(NUMBER_OF_BLOCK_ROWS, { Array(NUMBER_OF_COLUMNS, {0}) })

        var counter = result.size-1
        for(i in 0..block.size-1){
            result[i] = block[counter]
            counter--
        }
        return result
    }

    private fun  translateBlock(block: Array<Array<Int>>): Array<Array<Char>> {

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