class A {
    private fun foo() = "OK"

    fun bar() = (A::foo)(this)
}

fun box() = A().bar()
