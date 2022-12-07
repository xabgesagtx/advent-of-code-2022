fun main() {

    fun readDirectoryContent(iter: Iterator<String>, name: String): FSDir {
        println("Reading content for $name")
        val currentDir = FSDir(mutableListOf(), name)
        var currentLine = iter.next()
        while (!currentLine.isLeaveCommand) {
            println("Current command $currentLine")
            when {
                currentLine.isChangeDir -> currentDir.addItem(readDirectoryContent(iter, currentLine.dirName))
                currentLine.isFile -> currentDir.addItem(currentLine.toFile())
            }
            if (iter.hasNext()) {
                currentLine = iter.next()
            } else {
                break
            }
        }
        return currentDir
    }

    fun parseInput(input: List<String>): FSDir {
        val iter = input.iterator()
        return readDirectoryContent(iter, iter.next().dirName)
    }


    fun part1(input: List<String>): Int {
        val mainDir = parseInput(input)
        mainDir.print()
        return mainDir.allDirs.map { it.calculatedSize() }.filter { it < 100000 }.sum()
    }

    fun part2(input: List<String>): Int {
        val mainDir = parseInput(input)
        val spaceUsed = mainDir.calculatedSize()
        val spaceFree = 70000000 - spaceUsed
        val spaceToBeFreed = 30000000 - spaceFree
        return mainDir.allDirs.map { it.calculatedSize() }.filter { it > spaceToBeFreed }.min()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    println(part1(testInput))
    check(part1(testInput) == 95437)
    check(part2(testInput) == 24933642)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}

private val String.isChangeDir: Boolean
    get() = startsWith("$ cd ")

private val String.isFile: Boolean
    get() = this[0].isDigit()

private fun String.toFile(): FSFile {
    val (fileSize, name) = split(" ")
    return FSFile(fileSize.toInt(), name)
}

private val String.dirName: String
    get() = substringAfter("$ cd ").trim()

private val String.isLeaveCommand: Boolean
    get() = this == "$ cd .."

private interface FSItem {
    fun calculatedSize(): Int
    fun print(indent: Int = 0)
}

private data class FSFile(val size: Int, val name: String) : FSItem {
    override fun calculatedSize(): Int {
        return size
    }

    override fun print(indent: Int) {
        println("${" ".repeat(indent)} file $name $size")
    }
}

private data class FSDir(val items: MutableList<FSItem>, val name: String): FSItem {
    override fun calculatedSize(): Int = items.sumOf { it.calculatedSize() }

    override fun print(indent: Int) {
        println("${" ".repeat(indent)} dir $name")
        items.forEach{
            it.print(indent + 2)
        }
    }

    fun addItem(item: FSItem) = items.add(item)

    val allDirs: List<FSDir>
        get() = items.mapNotNull {
            it as? FSDir
        }.flatMap { listOf(it) + it.allDirs }
}
