import "styled-components";
import { IAppTheme } from "./UI/theme/AppTheme";

declare module "styled-components" {
  export interface DefaultTheme extends IAppTheme {}
}
