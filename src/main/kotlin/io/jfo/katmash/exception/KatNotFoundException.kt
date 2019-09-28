package io.jfo.katmash.exception

import java.lang.RuntimeException

class KatNotFoundException(id: Int): RuntimeException("The kat with the id $id was not found!!!")