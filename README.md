# Wing Tweaks (client-side add-on for Fantastic Wings)

Fabric, Minecraft 26.3. Client only: the server does not need it.

## Features
- `/wg wings off` hides YOUR Fantastic Wings on YOUR screen. `/wg wings on` shows them again.
  `/wg wings` toggles. The setting is saved.
- Offhand `paper` cosmetic wings always stay on your back in the standing position: while flying, elytra,
  swimming, crawling, falling after you stop flying, and while walking. Only offhand paper is affected.
  (While sneaking the normal pose is used, since it already follows the body lean.)
- `/wg wings all off` hides EVERYONE's Fantastic Wings (yours too) on YOUR screen. `/wg wings all on` shows them again.
  `/wg wings all` toggles. The setting is saved. Only the Fantastic Wings are hidden, not the offhand paper cosmetic.
- `/wingtweaks wings [on|off]` does the same as `/wg wings`.

## Build
1. Open `gradle.properties` and check `loom_version`, `loader_version`, `fabric_api_version`
   against https://fabricmc.net/develop (select Minecraft 26.3).
2. Push this folder to a GitHub repo. Go to the **Actions** tab -> **build** -> download the `wingtweaks-jar` artifact.
3. Put the jar in your `mods` folder next to Fantastic Wings.

## Tuning (config/wingtweaks.properties)
- `paper_arm_x_deg` / `paper_arm_z_deg`: if the paper sits a bit off compared to standing, adjust these (default -18 / 0).
- `tilt_start_deg` / `tilt_full_deg`: how far the body must tilt before the fix blends in.
- `paper_fix=false` turns the paper fix off.
- `paper_always_lock=false` goes back to only fixing the paper while the body is tilted.
