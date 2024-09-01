import { ReactNode } from "react";

export function If({ children, condition }: IfProps) {
  return condition ? children : null;
}
interface IfProps {
  condition: boolean;
  children: ReactNode;
}
