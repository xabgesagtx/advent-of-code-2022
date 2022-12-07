fun main() {

    fun readDirectoryContent(iter: Iterator<String>, name: String): Dir {
        println("Reading content for $name")
        val currentDir = Dir(mutableListOf(), name)
        var currentLine = iter.next()
        while (!currentLine.isLeaveCommand) {
            println("Current command $currentLine")
            when {
                currentLine.isChangeDir -> currentDir.items.add(readDirectoryContent(iter, currentLine.dirName))
                !currentLine.isListCommand && !currentLine.isDir -> currentDir.items.add(currentLine.toFile())
            }
            if (iter.hasNext()) {
                currentLine = iter.next()
            } else {
                break
            }
        }
        return currentDir
    }

    fun parseInput(input: List<String>): Dir {
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
    get() = this.startsWith("$ cd ")

private val String.isDir: Boolean
    get() = this.startsWith("dir ")

private fun String.toFile(): File {
    val (fileSize, name) = this.split(" ")
    return File(fileSize.toInt(), name)
}

private val String.dirName: String
    get() = this.substringAfter("$ cd ").trim()

private val String.isListCommand: Boolean
    get() = this == "$ ls"
private val String.isLeaveCommand: Boolean
    get() = this == "$ cd .."

private interface FileSystemItem {
    fun calculatedSize(): Int
    fun print(indent: Int = 0): Unit
}

private data class File(val size: Int, val name: String) : FileSystemItem {
    override fun calculatedSize(): Int {
        return size
    }

    override fun print(indent: Int) {
        println("${" ".repeat(indent)} file $name $size")
    }
}

private data class Dir(val items: MutableList<FileSystemItem>, val name: String): FileSystemItem {
    override fun calculatedSize(): Int {
        return items.sumOf { it.calculatedSize() }
    }

    override fun print(indent: Int) {
        println("${" ".repeat(indent)} dir $name")
        items.forEach{
            it.print(indent + 2)
        }
    }

    val allDirs: List<Dir>
        get() {
            return items.mapNotNull {
                it as? Dir
            }.flatMap { listOf(it) + it.allDirs }
        }
}