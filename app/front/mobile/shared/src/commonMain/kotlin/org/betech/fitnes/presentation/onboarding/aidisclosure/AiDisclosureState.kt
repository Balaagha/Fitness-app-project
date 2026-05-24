package org.betech.fitnes.presentation.onboarding.aidisclosure

/**
 * State for AI Disclosure screen (Pencil eQcvv).
 * @param isAcknowledging true while the analytics ack + nav is in flight.
 */
data class AiDisclosureState(val isAcknowledging: Boolean = false)
