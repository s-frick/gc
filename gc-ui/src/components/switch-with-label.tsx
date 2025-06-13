import { Label } from "@/components/ui/label"
import { Switch } from "@/components/ui/switch"

export function SwitchWithLabel({ label, id, checked, onCheckedChange }: { label: string, id: string, checked?: boolean, onCheckedChange: (c: boolean) => void }) {
  return (
    <div className="flex items-center space-x-2">
      <Switch
        id={id}
        checked={checked}
        onCheckedChange={onCheckedChange}
      />
      <Label htmlFor={id}>{label}</Label>
    </div>
  )
}

