package components

import com.raquo.laminar.api.L.*

object ThreeStateSwitch {

  enum State:
      case Negative, Neutral, Positive

  def render(_label: String, state: Var[State]): HtmlElement =

    val backgroundSignal = state.signal.map {
      case State.Negative => "bg-red-200"
      case State.Neutral => "bg-gray-200"
      case State.Positive => "bg-green-200"
    }

    div(
      cls("inline-flex relative items-center justify-between"),
      span(
        cls("ml-3 text-sm mr-4"),
        _label
      ),
      div(
        cls("flex flex-row cursor-pointer rounded w-11 h-5 border-gray-300"),
        cls <-- backgroundSignal,
        div(
          cls("rounded basis-1/3 py-1 pl-1"),
          cls.toggle("bg-slate-50") <-- state.signal.map(_ == State.Negative),
          onClick.map(_ => State.Negative) --> state.writer
        ),
        div(
          cls("rounded basis-1/3 py-1"),
          cls.toggle("bg-slate-50") <-- state.signal.map(_ == State.Neutral),
          onClick.map(_ => State.Neutral) --> state.writer
        ),
        div(
          cls("rounded basis-1/3 py-1 pr-1"),
          cls.toggle("bg-slate-50") <-- state.signal.map(_ == State.Positive),
          onClick.map(_ => State.Positive) --> state.writer
        )
      )
    )
}
