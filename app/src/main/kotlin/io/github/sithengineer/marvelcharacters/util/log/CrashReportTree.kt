package io.github.sithengineer.marvelcharacters.util.log

import timber.log.Timber

class CrashReportTree : Timber.Tree() {
  override fun log(priority: Int, tag: String?, message: String?, t: Throwable?) {
    // TODO log important information for a (crash) reporting system
  }
}