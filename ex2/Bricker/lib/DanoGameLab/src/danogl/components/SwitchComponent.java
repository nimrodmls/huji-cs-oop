package danogl.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BooleanSupplier;

/**
 * A very simple component that receives a map between conditions and components.
 * If a condition is held in a given frame, the corresponding component is updated.
 * @author Dan Nirel
 */
public class SwitchComponent implements Component {
    private final List<BooleanSupplier> conditions;
    private final List<Component> components;
    private final Component defaultComponent;
    private final boolean mutuallyExclusive;

    /**
     * construct a SwitchComponent with multiple conditions and corresponding components,
     * that are either mutually exclusive or not, with the option of specifying a default option.
     * @param conditions if a condition holds in a given frame,
     *                                the corresponding component may be updated (depending on
     *                   the value of the parameter mutuallyExclusive and other conditions the hold).
     *                   None of the conditions may be null.
     * @param components if the corresponding condition of a given component in this list is held
     *                   in a given frame, the component may be updated. The length of this list
     *                   must be the same length as the previous. Elements in this list may be null.
     * @param defaultComponent if none of the conditions hold in a given frame, this component is updated.
     *                         can be null to specify no default component.
     * @param mutuallyExclusive if true, and a condition holds in a given frame,
     *                          no further conditions are checked (even if the component is null).
     *                          If false, any component whose condition is held is updated (unless
     *                          it is null).
     *                          In any case, if any of the conditions in the parameter conditions
     *                          hold, the default component is NOT updated, and if none of them hold
     *                          the default component is updated (unless it is null).
     */
    public SwitchComponent(
            List<BooleanSupplier> conditions,
            List<Component> components,
            Component defaultComponent,
            boolean mutuallyExclusive) {
        this.conditions = conditions;
        this.components = components;
        this.defaultComponent = defaultComponent;
        this.mutuallyExclusive = mutuallyExclusive;
    }

    public SwitchComponent(BooleanSupplier condition, Component componentIfHolds,
                           Component componentIfNotHolds) {
        this(List.of(condition), Collections.singletonList(componentIfHolds),
                componentIfNotHolds, true);
    }

    @Override
    public void update(float deltaTime) {
        boolean someCondWasTrue = false;
        for(int i = 0 ; i < conditions.size() ; i++) {
            if(conditions.get(i).getAsBoolean()) {
                Component component = components.get(i);
                if(component != null)
                    component.update(deltaTime);
                someCondWasTrue = true;
                if(mutuallyExclusive)
                    break;
            }
        }
        if(!someCondWasTrue && defaultComponent != null)
            defaultComponent.update(deltaTime);
    }
}
