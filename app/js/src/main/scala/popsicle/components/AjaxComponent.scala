package popsicle.components

import popsicle.components.backend.ajax.AjaxBackend

import japgolly.scalajs.react.{ReactElement, BackendScope, ReactComponentB}

abstract class AjaxComponent[State](backendFactory: BackendScope[_, State] => AjaxBackend[State]) {
  
  def initState: State
  def renderState(state: State): ReactElement

  def buildComponent = ReactComponentB[Unit]("ajax-component")
    .initialState(initState)
    .backend(backendFactory(_))
    .render($ => renderState($.state))
    .componentDidMount(_.backend.init())
    .componentWillUnmount(_.backend.close())
    .buildU
}
