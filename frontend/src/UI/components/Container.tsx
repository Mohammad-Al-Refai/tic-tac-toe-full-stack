import { ReactNode } from "react";

export function Container({ children }: ContainerProps) {
  return (
    <div className="flex w-100 col align-items-center p-xl5">{children}</div>
  );
}
interface ContainerProps {
  children: ReactNode;
}
