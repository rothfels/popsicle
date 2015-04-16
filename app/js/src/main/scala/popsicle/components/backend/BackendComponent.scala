package popsicle.components.backend

import japgolly.scalajs.react.{ReactElement, ReactComponentB, BackendScope}

/**
* Base class for stateful react component backend.
*
* @param $ BackendScope injected by react component
*/
abstract class Backend($: BackendScope[_, _]) {
  def init(): Unit
  def close(): Unit
}

/**
* Base class for stateful react component with a backend
*
* @param backendFactory injected backend factory
* @tparam State underlying component state
*/
abstract class BackendComponent[State]
  (backendFactory: BackendScope[_, State] => Backend) {

  def initState: State
  def renderState(state: State): ReactElement
  def componentName: String

  def buildComponent = ReactComponentB[Unit](componentName)
    .initialState(initState)
    .backend(backendFactory(_))
    .render($ => renderState($.state))
    .componentDidMount(_.backend.init())
    .componentWillUnmount(_.backend.close())
    .buildU
}

///**
// * Base class for stateful react component (constructed with props) with a backend
// *
// * @param backendFactory injected backend factory
// * @tparam Props component constructor arg
// * @tparam State underlying component state
// */
//abstract class BackendComponentP[Props, State]
//  (backendFactory: BackendScope[Props, State] => Backend) {
//
//  def initState: State
//  def renderState(state: State): ReactElement
//  def componentName: String
//
//  def buildComponent = ReactComponentB[Props](componentName)
//    .initialState(initState)
//    .backend(backendFactory(_))
//    .render($ => renderState($.state))
//    .componentDidMount(_.backend.init())
//    .componentWillUnmount(_.backend.close())
//    .build
//}
//

