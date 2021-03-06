package org.broadinstitute.sting.queue.engine

import org.broadinstitute.sting.queue.function.InProcessFunction
import java.util.Date
import org.broadinstitute.sting.queue.util.{Logging, IOUtils}
import org.broadinstitute.sting.utils.Utils

/**
 * Runs a function that executes in process and does not fork out an external process.
 */
class InProcessRunner(val function: InProcessFunction) extends JobRunner[InProcessFunction] {
  private var runStatus: RunnerStatus.Value = _

  def start() = {
    getRunInfo.startTime = new Date()
    getRunInfo.exechosts = Utils.resolveHostname()
    runStatus = RunnerStatus.RUNNING

    function.run()

    getRunInfo.doneTime = new Date()
    val content = "%s%nDone.".format(function.description)
    IOUtils.writeContents(function.jobOutputFile, content)
    runStatus = RunnerStatus.DONE
  }

  def status = runStatus
}
