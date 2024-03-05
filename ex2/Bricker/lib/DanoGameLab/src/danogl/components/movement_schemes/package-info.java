/**
 * The public classes in the package are components that can be added to GameObjects in order to
 * control their movement/direction according to various popular schemes.
 * The MovementSchemes are suited to either player-driven avatars, AI agents, or any other GameObject
 * which should move according to some conditions in user-input or in the world.
 * <br>Note that objects with predictable and repeating movement, such as a platform
 * moving regularly in a fixed pattern, might be better served by the
 * {@link danogl.components.Transition} component instead of a MovementScheme.
 * @author Dan Nirel
 */
package danogl.components.movement_schemes;