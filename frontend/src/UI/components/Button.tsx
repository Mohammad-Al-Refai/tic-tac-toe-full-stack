import styled from "styled-components";
import { ThemeColors } from "../theme/ThemeTypes";
import { ReactNode } from "react";

const StyledButton = styled.button<{
  $background: ThemeColors;
  $color: ThemeColors;
}>`
  margin-top: ${(props) => props.theme.YAxisSpacing.M};
  font-size: ${(props) => props.theme.fontSize.L};
  background-color: ${(props) => props.theme.colors[props.$background]};
  border-radius: ${(props) => props.theme.Surroundings.M};
  border: unset;
  cursor: pointer;
  padding: ${(props) => props.theme.Surroundings.M};
  color: ${(props) => props.theme.colors[props.$color]};
`;

export function Button({ variant, children, onClick, className }: ButtonProps) {
  switch (variant) {
    case "primary":
      return (
        <StyledButton
          className={className}
          $background="primary"
          $color="onPrimary"
          onClick={onClick}
        >
          {children}
        </StyledButton>
      );
    case "secondary":
      return (
        <StyledButton
          className={className}
          $background="secondary"
          $color="onSecondary"
          onClick={onClick}
        >
          {children}
        </StyledButton>
      );
    case "tertiary":
      return (
        <StyledButton
          className={className}
          $background="tertiary"
          $color="onTertiary"
          onClick={onClick}
        >
          {children}
        </StyledButton>
      );
    default:
      break;
  }
}

interface ButtonProps {
  variant: ThemeColors;
  children: ReactNode;
  onClick: () => void;
  className?: string;
}
