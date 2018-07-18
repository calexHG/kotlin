// Auto-generated by org.jetbrains.kotlin.generators.tests.GenerateRangesCodegenTestData. DO NOT EDIT!
// WITH_RUNTIME


fun box(): String {
    val list1 = ArrayList<Int>()
    val range1 = (3..9 step 2).reversed()
    for (i in range1) {
        list1.add(i)
        if (list1.size > 23) break
    }
    if (list1 != listOf<Int>(9, 7, 5, 3)) {
        return "Wrong elements for (3..9 step 2).reversed(): $list1"
    }

    val list2 = ArrayList<Int>()
    val range2 = (3.toByte()..9.toByte() step 2).reversed()
    for (i in range2) {
        list2.add(i)
        if (list2.size > 23) break
    }
    if (list2 != listOf<Int>(9, 7, 5, 3)) {
        return "Wrong elements for (3.toByte()..9.toByte() step 2).reversed(): $list2"
    }

    val list3 = ArrayList<Int>()
    val range3 = (3.toShort()..9.toShort() step 2).reversed()
    for (i in range3) {
        list3.add(i)
        if (list3.size > 23) break
    }
    if (list3 != listOf<Int>(9, 7, 5, 3)) {
        return "Wrong elements for (3.toShort()..9.toShort() step 2).reversed(): $list3"
    }

    val list4 = ArrayList<Long>()
    val range4 = (3.toLong()..9.toLong() step 2.toLong()).reversed()
    for (i in range4) {
        list4.add(i)
        if (list4.size > 23) break
    }
    if (list4 != listOf<Long>(9, 7, 5, 3)) {
        return "Wrong elements for (3.toLong()..9.toLong() step 2.toLong()).reversed(): $list4"
    }

    val list5 = ArrayList<Char>()
    val range5 = ('c'..'g' step 2).reversed()
    for (i in range5) {
        list5.add(i)
        if (list5.size > 23) break
    }
    if (list5 != listOf<Char>('g', 'e', 'c')) {
        return "Wrong elements for ('c'..'g' step 2).reversed(): $list5"
    }

    return "OK"
}
