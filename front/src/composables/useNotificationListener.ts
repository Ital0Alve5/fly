import { watch } from 'vue'
import { useToast } from '@/components/ui/toast/use-toast'
import { useGlobalStore } from '@/stores/global'

export function useNotificationListener() {
  const global = useGlobalStore()
  const { toast } = useToast()

  watch(
    () => global.notification,
    (newNotification) => {
      if (newNotification) {
        toast(newNotification)
      }
    },
    { immediate: true },
  )
}
